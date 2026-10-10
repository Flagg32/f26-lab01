# 里程碑3 验证记录：识别错误的智能体生成

## 1. 补丁内容

`changes/agent-attempt.patch` 为 `BookingService` 新增了一个 `isAvailable` 方法，用于查询房间在指定时间段是否可用。

智能体提交的原始（有缺陷）实现：

```java
public boolean isAvailable(Room room, TimeInterval interval) {
    for (Booking b : store.bookingsForRoom(room)) {
        if (b.interval().start() >= interval.start()
                && b.interval().start() < interval.end()) {
            return false;
        }
    }
    return true;
}
```

## 2. 缺陷分析

该方法只检查已有预订的开始时间（start）是否落在查询区间内，而没有判断整个时间段是否重叠。它没有复用 `TimeInterval.overlaps()`，违背了 ARCHITECTURE.md 中"overlaps 是冲突规则唯一所在"的设计原则。

以查询区间 Q = [600, 660)（10:00-11:00）为例，对 9 种位置关系进行手算验证：

| 编号 | 已有预订 E | 真实是否冲突 | 补丁返回 | 是否正确 |
| --- | --- | --- | --- | --- |
| 1 | [480,540) 完全在 Q 之前 | 否 | true（可用） | 正确 |
| 2 | [540,600) 首尾相接在 Q 前 | 否 | true | 正确 |
| 3 | [570,630) 先开始延伸进 Q | 是 | true（误判可用） | 错误 |
| 4 | [540,720) 完全包含 Q | 是 | true（误判可用） | 错误 |
| 5 | [610,640) 被 Q 包含 | 是 | false | 正确 |
| 6 | [630,720) 在 Q 内开始延伸出 Q | 是 | false | 正确 |
| 7 | [600,660) 与 Q 完全相同 | 是 | false | 正确 |
| 8 | [660,720) 首尾相接在 Q 后 | 否 | true | 正确 |
| 9 | [700,760) 完全在 Q 之后 | 否 | true | 正确 |

第 3、4 种情况会被错误判定为"可用"，若调用方据此发起预订，将破坏核心不变式（同一房间出现两个重叠的已确认预订）。

## 3. 暴露问题的测试

`IsAvailableTest.java` 包含以下测试：

1. `notAvailableWhenAnEarlierBookingRunsIntoTheRequest`：已有预订 [570,630) 先开始并延伸进查询区间 [600,660)，断言房间不可用。
2. `notAvailableWhenAnExistingBookingContainsTheRequest`：已有预订 [540,720) 完全包含查询区间，断言房间不可用。
3. `isAvailableAgreesWithBook`：验证 isAvailable 的结论与随后 book() 的实际结果一致。

前两个测试在缺陷代码上运行会失败，在修复后的代码上运行通过。

## 4. 修复方案

让 isAvailable 复用领域层唯一的冲突规则 TimeInterval.overlaps：

```java
public boolean isAvailable(Room room, TimeInterval interval) {
    for (Booking b : store.bookingsForRoom(room)) {
        if (b.interval().overlaps(interval)) {
            return false;
        }
    }
    return true;
}
```

## 5. 验证结果

全部 20 个单元测试通过：
- TimeIntervalTest：5 个
- InMemoryBookingStoreTest：1 个
- BookingServiceTest：6 个
- CancelBookingTest：5 个
- IsAvailableTest：3 个

核心不变式得到保障：每个房间的已确认预订两两不重叠。

## 6. 为什么补丁能"通过所有测试"

原始补丁应用后，现有测试套件中没有任何测试调用 isAvailable，因此该方法的缺陷不会被原有测试发现。这说明：
- "测试全绿"不等于"代码正确"；
- 新增代码必须配套新的测试，且测试要能在缺陷代码上失败；
- 信任一次改动，必须先理解并独立验证，而不是依赖"智能体让测试变绿"。

## 7. 结论

补丁已采用修复路线：复用 TimeInterval.overlaps，不另造冲突规则。错误代码、暴露问题的测试、修复提交在提交历史中均有清晰记录。
