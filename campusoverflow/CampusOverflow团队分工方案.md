# CampusOverflow 团队分工与实施手册

> 依据：《CampusOverflow 软件工程教学方案》（湖北文理学院 计算机工程学院 · 计算机科学与技术专业 · 主讲教师 胡春阳）
> 覆盖范围：16 周 / 48 学时 / 4 个 Sprint + 8 次实验 / 期末答辩
> 适用对象：课程团队（建议 5 人，兼容 4 人与 6 人编制）

---

## 0. 使用说明

| 项 | 说明 |
| --- | --- |
| 编制基准 | 本手册以 **5 人团队** 为基准编制；第 1.2 节给出 4 人 / 6 人编制的合并与拆分规则 |
| 阅读方式 | 第 1–3 章全员通读；第 4 章按"成员代号"各取所需；第 5 章为 Sprint 执行清单；第 7 章用于对照成绩 |
| 术语 | PO = Product Owner，SM = Scrum Master，Dev = Developer，QA = 质量保障（由 Dev 兼任） |
| 硬约束 | 文档第七章：团队过程性表现 30% + Sprint 增量演示 25% + 个人代码贡献 15% + 期末答辩 20% + 个人理论 10%，加分项封顶 +5% |

**核心原则（教学方案 3.1）**：理论课在前、实验课在后是反工程教育规律的做法；本课程刻意将实验前置——先在工程上"撞墙"，再在理论课获得方法论"破墙"工具。因此**每次 Lab 的产出必须在当周理论课之前准备就绪**。

---

## 1. 团队编制与角色总表

### 1.1 推荐编制（5 人）

| 代号 | 角色 | 兼任 | 技术主场 | 一句话职责 | 对应文档条目 |
| --- | --- | --- | --- | --- | --- |
| **A** | PO（产品负责人） | 前端 Dev（Identity / Discovery 前端） | React + TS、需求分析 | 让"做对的事"发生：Backlog、验收、演示 | 附录 F.1 |
| **B** | SM（Scrum Master） | DevOps / 部署负责人 | GitHub Actions、Docker、Prometheus | 让"事做得下去"：流程、看板、CI/CD、上线 | 附录 F.2 |
| **C** | 后端领域负责人 | Dev（Identity + Q&A 子域） | Spring Boot 3、DDD、JPA | 领域建模与核心聚合，工程骨架奠基 | 附录 F.3 |
| **D** | 后端实现工程师 | Dev（Reputation + Discovery 子域） | Spring Boot、Redis、查询优化 | 互动/声誉/发现子域落地与集成 | 附录 F.3 |
| **E** | 前端工程师 | **QA**（测试与质量） | React + TS、Vitest、Playwright | 前端工程质量 + 全链路测试与门禁 | 附录 F.3 / F.4 |

> **为什么把 QA 交给 E**：文档 F.4 明确 QA 由 Dev 兼任。E 同时掌握前端与 E2E 工具链（Vitest / Playwright），是"测试金字塔三层"中最合适的落地人；后端单元/集成测试仍由 C、D 各自负责，E 只做**标准制定 + 抽查 + 端到端**。

### 1.2 4 人 / 6 人编制的映射

**4 人团队（最小可行编制）**

| 代号 | 由谁承担 | 调整说明 |
| --- | --- | --- |
| A′ | 原 A（PO 兼前端负责人） | 前端全部页面由 A′ 独担；Discovery 前端（搜索/筛选/推荐）降级为 P2，Sprint 3 内只做**筛选 + LIKE 搜索** |
| B′ | 原 B（SM + DevOps） | 不变；额外承接原 E 的 SonarQube 门禁配置 |
| C′ | 原 C（后端领域） | 不变 |
| D′ | 原 D（后端实现） **兼 QA** | 承接原 E 的 Playwright E2E 与测试报告；单元测试分工不变 |

**6 人团队（充裕编制）**

| 代号 | 角色 | 说明 |
| --- | --- | --- |
| A–D | 同 5 人编制 | 不变 |
| **E** | 前端 Dev（Q&A / Reputation 前端） | 只做前端，不兼 QA |
| **F** | QA（专职测试与质量） | 承接 E 原 QA 职责，另加**回归测试 + Go/No-Go 建议** |

### 1.3 角色边界与反模式（文档 L5 明令）

| 反模式 | 禁止原因 | 正确做法 |
| --- | --- | --- |
| PO 兼 SM | PO 追价值最大化、SM 追流程健康，目标冲突 | PO 与 SM 必须由两人分任 |
| SM 兼 Dev | SM 要"仆人式领导、清除障碍"，兼 Dev 会出现自我评审 | SM 可由 Dev 转任，但 Sprint 内不得既是 SM 又是代码 Owner；本手册中 B 的技术产出限定在 `deploy/` 与 `.github/` |
| PO 当"需求录入员" | Backlog 是价值排序，不是需求清单 | PO 必须给出**优先级 + 验收标准 + 拒绝理由** |
| 看板长期不更新 | 直接扣 Sprint Review"流程纪律 15%" | 卡片状态变更与代码提交同步发生 |
| QA 只在最后测试 | 违反测试金字塔与 TDD 要求 | 测试标准在 Sprint 开始前定义，缺陷当周入库 |

---

## 2. 全域协作机制（全员共同遵守）

### 2.1 Git 协作流程（Trunk-Based + PR）

```
main  ← 受保护分支，禁止直推
 ├── feature/<模块>-<姓名拼音>-<简述>   功能分支，单项任务一条
 ├── fix/<模块>-<简述>                  缺陷修复
 ├── docs/<简述>                        纯文档
 └── hotfix/<简述>                      上线后应急
```

**标准操作步骤（每位成员每次提交都走一遍）**

1. `git switch main && git pull --rebase`
2. `git switch -c feature/qa-zhangsan-vote-api`
3. 小步提交，每个 commit 只做一件事，提交信息遵循 `feat|fix|docs|test|refactor|chore(scope): subject`
4. 本地跑通：后端 `mvn -q clean verify`，前端 `pnpm lint && pnpm typecheck && pnpm test`
5. `git push -u origin <分支>`
6. 在 GitHub/Gitee 发起 PR，填写：**关联故事 ID / 变更说明 / 自测证据 / 截图 / 风险**
7. CI 全绿 + 至少 1 名队友 LGTM（按第 2.5 节"写权限地图"指定评审人）
8. SM（B）合并，删除远端分支；合并方式统一 **Squash Merge**
9. 看板卡片移入 Done，并在 PR 描述中回填"验收人 + 验收结论"

> 红线：**任何人不得直接 push main**。Lab 1 第一步即配置分支保护（main 不允许直推、PR 必须经评审）。

### 2.2 提交与评审规约

- 一条 PR ≤ 400 行有效变更（超出请拆分）；
- PR 标题必须能独立讲清"做了什么"，禁止 `update`、`fix bug`；
- 评审依据文档**附录 D**：正确性 / 可读性 / 可测试性 / 安全 / 性能 / 风格，六类逐项确认；
- 每条 PR 至少 5 条实质性评审意见（Lab 7 提交物要求）；
- 评审用语：`LGTM`（可合并）、`Nit:`（建议）、`Blocker:`（必须改）。

### 2.3 会议节奏（文档 3.4）

| 会议 | 时间 | 主持人 | 时长 | 必产出的文件 |
| --- | --- | --- | --- | --- |
| Sprint Planning | 每个 Sprint 第一周周一 | PO（A） | 40 min | `docs/backlog/sprint-N-plan.md` |
| Daily Standup | 每次实验课前 10 分钟轮换主持 | SM（B） | 10 min | 每日纪要追加到 `docs/team/standup-log.md` |
| Sprint Review | 每个 Sprint 最后一周 | PO（A）演示，SM（B）控场 | 每队 5 min | `docs/review/sprint-N-review.md` |
| Sprint Retrospective | Review 次日 | SM（B） | 25 min | `docs/retro/sprint-N.md` |

轮换主持规则：Standup 按 A→B→C→D→E 顺序轮换，训练"人人都是流程主人"。

### 2.4 Definition of Done（文档附录 E，全员生效）

一个故事只有**同时**满足以下 9 条才算 Done：

1. 代码实现完成并合并到 `main`；
2. 单元测试覆盖率 ≥ 70%，核心领域 ≥ 90%；
3. CI 全绿（构建、测试、静态分析、安全扫描）；
4. 至少 1 名队友 PR 评审 LGTM；
5. Gherkin 验收标准全部通过；
6. 已部署到测试环境并通过 Smoke Test；
7. OpenAPI 契约同步更新；
8. 涉及架构决策的已写 ADR；
9. 用户文档与 README 已更新。

### 2.5 写权限地图（避免同文件冲突）

| 路径 | Owner（唯一） | 共同作者（需 Owner 同意） |
| --- | --- | --- |
| `backend/pom.xml`、`backend/co-bootstrap/**` | C | D |
| `backend/co-shared-kernel/**` | C | D |
| `backend/co-identity/**`、`backend/co-qa/**` | C | D（只读 + PR 建议） |
| `backend/co-reputation/**`、`backend/co-discovery/**` | D | C（只读 + PR 建议） |
| `frontend/src/features/identity/**`、`features/discovery/**`、`pages/**` | A | E |
| `frontend/src/features/qa/**`、`features/reputation/**` | E | A |
| `frontend/src/api/**`（OpenAPI 自动生成） | E | D（生成源维护） |
| `frontend/src/shared/**` | E | A |
| `deploy/**`、`.github/workflows/**`、`frontend/Dockerfile` | B | 无 |
| `docs/backlog/**`、`docs/templates/**` | A | 全员 |
| `docs/domain/**` | C | D |
| `docs/api/**`（openapi.yaml） | D | C |
| `docs/adr/**` | 决策提出人 | 全员 |
| `docs/team/**`、`docs/retro/**`、`docs/review/**` | B | 全员 |
| `README.md` | B | A |

### 2.6 工具与环境清单（Lab 1 前全部装好）

| 类别 | 工具 | 负责人 |
| --- | --- | --- |
| 版本控制 | Git 2.40+、GitHub/Gitee 账号并加入课程组织 | 全员，B 校验 |
| 后端 | JDK 21、IntelliJ IDEA、Maven、MySQL 8 / Docker MySQL | C、D |
| 前端 | Node.js 20+、pnpm、VS Code / WebStorm | A、E |
| 容器与流水线 | Docker Desktop、GitHub Actions（或 Gitee Go）、SonarQube | B |
| 看板与文档 | Gitee 项目 / GitHub Projects、团队 IM 群、共享文档库 | B |
| 测试 | JUnit 5、Mockito、Testcontainers、Vitest、Playwright | E 统一版本，C/D 使用 |

---

## 3. 逐周分工总表（W1–W16）

> 标注方式：▲ 主责人，△ 参与人

| 周 | 理论 / 实验 | Sprint | 关键交付物 | A（PO） | B（SM） | C（后端领域） | D（后端实现） | E（前端 / QA） |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| W1 | L1 | Sprint 0 | 项目组建、Git 仓库建立 | ▲ 愿景宣讲 | ▲ 仓库/账号 | △ 技能矩阵 | △ 技能矩阵 | △ 技能矩阵 |
| W2 | L2 + **Lab 1** | Sprint 0 | 团队章程 v1.0、看板、首次 PR | △ 角色承诺书 | ▲ 工具链/看板 | ▲ hello PR | ▲ hello PR | ▲ hello PR |
| W3 | L3 + **Lab 2** | Sprint 1 | 用户故事地图、Product Backlog | ▲ 故事地图主持 | △ 白板/纪要 | △ 技术故事 | △ 技术故事 | △ UI 故事 |
| W4 | L4 | Sprint 1 | Sprint 1 计划、Sprint Backlog | ▲ Planning | ▲ 看板落卡 | △ 估点 | △ 估点 | ▲ Gherkin 验收标准 |
| W5 | L5 + **Lab 3** | Sprint 1 | 限界上下文图、聚合清单、术语表 | △ 价值链对齐 | △ 现场记录 | ▲ 事件风暴主持 | ▲ 聚合识别 | △ 前端上下文 |
| W6 | L6 | Sprint 1 | Sprint 1 评审 + 回顾 | ▲ Review | ▲ Retro | ▲ 演示后端 | △ 演示数据 | △ 演示前端 |
| W7 | L7 + **Lab 4** | Sprint 2 | 多模块工程可启动、POST /api/v1/questions | △ 接口清单 | △ 本地 DB 容器 | ▲ 工程骨架 | ▲ 接口贯通 | △ Postman 校验 |
| W8 | L8 | Sprint 2 | 核心聚合代码原型 | △ 验收 | △ 进度跟踪 | ▲ Question/Answer/Vote | ▲ Repository 实现 | △ UI 草图 |
| W9 | L9 + **Lab 5** | Sprint 2 | React + TS 工程可启动并对接后端 | △ 页面验收 | △ 质量脚本 | △ 接口联调 | △ 接口联调 | ▲ 前端骨架 + API 客户端 |
| W10 | L10 | Sprint 2 | Sprint 2 评审、OpenAPI 契约 | ▲ Review | ▲ 契约 CI 校验 | △ 契约评审 | ▲ openapi.yaml | △ 类型生成 |
| W11 | L11 + **Lab 6** | Sprint 3 | 问答端到端流程可演示 | ▲ Review | △ 环境保障 | ▲ 采纳/标签 | ▲ 投票/评论/声誉 | ▲ 前端页面 |
| W12 | L12 | Sprint 3 | 单元测试编写规范 | △ | △ | ▲ 领域测试 | ▲ 应用层测试 | ▲ 测试规范文档 |
| W13 | L13 + **Lab 7** | Sprint 3 | 测试报告、PR 评审记录、质量门禁 | △ 评审 | ▲ Sonar/CI | ▲ 覆盖率补齐 | ▲ 覆盖率补齐 | ▲ 评审组织 + 清单 |
| W14 | L14 | Sprint 4 | GitHub Actions 流水线 | △ 需求补充 | ▲ 流水线 | △ 构建修复 | △ 构建修复 | △ 前端 Job |
| W15 | L15 + **Lab 8** | Sprint 4 | 系统在线可访问 + 监控面板 | △ 上线通告文案 | ▲ 部署上线 | ▲ 性能调优 | ▲ 缓存/索引 | ▲ E2E + 回归 |
| W16 | L16 | 答辩周 | 团队答辩、个人反思报告 | ▲ 演示脚本 | ▲ 流程材料 | ▲ 后端讲解答辩 | ▲ 数据讲解答辩 | ▲ 前端讲解答辩 |

---

## 4. 成员任务书

> 每位成员按本节执行。每节结构：**职责定位 → 全程任务 → 分 Sprint 详细步骤 → 交付物 → 考核关联 → 自查清单**。

### 4.1 成员 A —— PO（产品负责人）+ 前端 Dev（Identity / Discovery）

#### 职责定位
对**产品价值最大化**负责（附录 F.1）。不是需求录入员，而是：需求来源、优先级裁判、验收人、对外（教师/助教）沟通窗口。

#### 全程任务

- 维护 Product Backlog，每条含：故事 ID、角色、价值、估算、优先级、状态、验收人；
- 主持 Sprint Planning（定义 Sprint Goal）与 Sprint Review（验收增量）；
- 组织 ≥ 3 份真实用户访谈（L3 课后作业）并沉淀为需求依据；
- 负责 Identity 与 Discovery 前端页面；
- 负责期末答辩的 Demo 脚本与演示节奏。

#### Sprint 0（W1–W2）

1. 撰写《产品愿景声明》1 页：为谁、解决什么、为什么现在、成功标准（提交 `docs/backlog/vision.md`）；
2. 竞品分析：Stack Overflow 校园化改造点（课程标签 / 师生认证 / 班级圈 / 悬赏 / 学风画像），产出对比表；
3. 依据技能矩阵与同学组队（4–6 人），填写《角色承诺书》并签字提交；
4. 完成 L1 课后个人任务：800 字《人月神话》读书笔记（重点回答"为什么向滞后的项目增加人手会让它更慢"）；
5. 与 C、D、E 共同产出 Backlog 初版 ≥ 20 条（Lab 1 看板截图中体现 ≥ 5 条初始 Backlog）。

#### Sprint 1（W3–W6）· 主题：身份与问答内核

1. **Lab 2 主导故事地图**：以"用户从未注册到成为活跃贡献者"为骨架；
   - 横轴：注册 → 浏览 → 提问 → 回答 → 互动 → 成长；
   - 纵轴：每个活动下的故事卡片；
   - 识别 Walking Skeleton 切片作为 Sprint 1 MVP；
2. 将故事卡转成规范用户故事 `作为<角色>，我希望<能力>，以便<价值>`，按 INVEST 自评；
3. 用"价值/成本"二维矩阵排序，锁定 Sprint 1 承诺；
4. 为 Sprint 1 每条故事写 ≥ 3 个 Gherkin 场景（快乐路径 / 边界 / 异常），提交 `docs/backlog/stories/<story-id>.feature`；
5. 前端产出：
   - 登录 / 注册 / 邮箱验证页（对接 `/api/v1/auth/**`）；
   - 学院 / 班级徽章展示组件（消费 C 提供的身份接口）；
6. W6 主持 Sprint 1 Review，用"注册 → 提问 → 列表 → 回答"完整闭环演示；
7. 主持 Sprint 1 Retrospective（Start / Stop / Continue）。

#### Sprint 2（W7–W10）· 主题：互动与声誉

1. 维护 Backlog：把 Review 反馈转为新故事并重排优先级；
2. 前端产出：问题列表页 `/questions`、问题详情页 `/questions/:id`、个人主页（声誉值 / 徽章）；
3. 与 D 对齐 `/api/v1/questions`、`/api/v1/users/{id}/reputation` 契约，验收 OpenAPI 文档；
4. W10 主持 Sprint 2 Review，重点演示"投票 → 声誉变化"的可观测链路。

#### Sprint 3（W11–W13）· 主题：发现与通知

1. 前端产出：按课程 / 标签 / 时间筛选、关键词搜索框、通知中心（回答提醒 / 采纳提醒 / @提醒）；
2. 明确"范围控制"：实时聊天、直播答疑、PDF 内嵌、AI 自动回答属**超出 MVP**，只在 Sprint 4 之后选做加分项；
3. 与教师 / 助教确认"教师悬赏"业务规则（积分来源、上限、结算时机）并写入 `docs/backlog/rules-reputation.md`；
4. W13 参与跨成员代码评审（评审对象由 E 指定，不得评审自己的代码）。

#### Sprint 4（W14–W15）· 主题：质量与部署

1. 编写 8 分钟 Demo 脚本（每个演示动作配一句"业务价值"解说词）；
2. 上线后向其他团队发出"友测"邀请，收集 ≥ 5 条外部反馈并归档；
3. 与 B 共同确认上线通告文案与访问地址。

#### 答辩周（W16）

1. 主持 8 分钟系统演示，覆盖完整业务闭环；
2. 参与"技术深度讲解"中需求/价值部分的讲解；
3. 独立回答评委关于**需求优先级取舍**与**验收标准演化**的 3 个问题。

#### 交付物清单
`docs/backlog/vision.md`、`docs/backlog/product-backlog.md`、`docs/backlog/stories/*.feature`、`docs/backlog/rules-reputation.md`、故事地图 PNG、`docs/review/sprint-1..4-review.md`、Demo 脚本、个人反思日志。

#### 考核关联
团队过程性表现（看板完成度 / 回顾会质量）、Sprint 增量演示（功能完整性 30%）、期末答辩（演示 + 个人问答）、个人反思日志 5%。

#### 自查清单
- Backlog 每条都有价值描述与验收标准，没有"待定"项？
- 本次 Review 演示的每个功能都能说出"解决了谁的问题"？
- 是否有超出 MVP 的功能未经确认就排入 Sprint？

---

### 4.2 成员 B —— SM（Scrum Master）+ DevOps / 部署负责人

#### 职责定位
仆人式领导、流程教练（不是项目经理）。保护团队不受干扰、清除障碍、维护看板、主持 Daily Standup 与 Retrospective（附录 F.2）。技术主场：CI/CD、容器化、部署、可观测性。

#### 全程任务

- 看板（To Do / In Progress / In Review / Done + WIP 限制）每日同步；
- 维护燃尽图与速率图，Review 时给出 Velocity 数据；
- 维护 `main` 分支保护与合并纪律；
- 负责 `.github/workflows/**` 与 `deploy/**`；
- 期末上线与运维手册。

#### Sprint 0（W1–W2）· Lab 1 主责

1. Fork 课程组织提供的 `campus-overflow-template` 仓库到团队组织；
2. 配置分支保护：`main` 不允许直推；PR 必须经 ≥ 1 人评审；启用状态检查（CI 未过禁止合并）；
3. 创建看板四列 + WIP 限制（In Progress ≤ 3、In Review ≤ 3）；
4. 建立团队 IM 群与共享文档库；
5. 主持首次 Daily Standup 并留存纪要（陷阱排查：SM 不主持会议是第一大坑）；
6. 校验每位成员完成 `docs/team/hello-<name>.md` 的 PR 合并（验收：`main` 含全体 hello 文档）；
7. 牵头产出《团队章程 v1.0》：团队价值观、沟通约定（响应时限 / 会议缺席规则）、开发规约、DoD 初版；全员以 commit message 签名。

#### Sprint 1（W3–W6）

1. 建立燃尽图基线，记录理想线与实际线每天偏差；
2. W5 在 Lab 3 现场担任记录员，把事件风暴的"热点"转为风险登记册 `docs/team/risk-log.md`；
3. 检查 Sprint 1 承诺完成度，汇总阻塞项并在 Retro 中提案改进（≥ 2–3 项纳入下个 Sprint）；
4. 维护 `docs/team/standup-log.md`，确保无人漏报。

#### Sprint 2（W7–W10）

1. 为团队提供本地依赖环境：`docker compose -f deploy/docker-compose.dev.yml up -d`（MySQL 8 + Redis 7），令 C、D 不因数据库版本问题浪费实验时间；
2. 在 CI 中加入 OpenAPI 契约校验步骤（`openapi.yaml` 变更必须通过 lint）；
3. 跟踪"接口联调"这唯一的跨模块阻塞点，及时升级到教师（Coach）求助。

#### Sprint 3（W11–W13）· Lab 7 主责

1. 部署 SonarQube（Docker）并配置质量门禁：Coverage > 70%、Bugs = 0、Vulnerabilities = 0；
2. 在 `pom.xml` 配置 `jacoco-maven-plugin` 生成 `jacoco.xml`；配置 `sonar-project.properties`；
3. 把这些作为 CI 必经步骤：`build → unit-test → jacoco → sonar → quality-gate`；
4. 汇总评审记录（≥ 4 份高质量 PR 评审）并导出 PDF。

#### Sprint 4（W14–W15）· Lab 8 主责

1. **后端镜像**：多阶段 Dockerfile，`builder(JDK + Maven) → runtime(JRE only)`；启用 jlink 自定义 JRE 或 distroless 基础镜像；以非 root 用户运行；
2. **前端镜像**：`builder(node) → runtime(nginx:alpine)`；配置 Nginx 反向代理 `/api → 后端`；SPA 路由 fallback；
3. **compose 编排**：app + MySQL + Redis + nginx + Prometheus + Grafana；健康检查 + `depends_on: condition: service_healthy`；`.env` 管理环境变量，CI 中通过 Secrets 注入；
4. **CI/CD 扩展**：`docker build → push to registry → SSH 到测试服务器 docker compose pull && up -d → Smoke Test（/actuator/health）`；
5. **监控**：Grafana Dashboard 至少 5 个核心指标（QPS、P95 延迟、错误率、JVM 堆、DB 连接数）；配置 ≥ 1 条告警（p95 > 500ms 或 5xx 错误率 > 1%）；
6. **运维手册 v1.0**：启动、停止、回滚、常见故障处置流程；
7. 规避三个已知陷阱：Dockerfile 硬编码数据库密码（改用 Secrets/环境变量）、容器跨主机网络不通（统一 Docker network）、部署后 404（Nginx + React Router fallback 先在本地验证）。

#### 答辩周（W16）

1. 汇编流程材料：看板截图、燃尽图、Velocity 趋势、Retro 纪要；
2. 负责"DevOps 关键设计决策"的 7 分钟技术讲解片段；
3. 独立回答评委关于流水线设计、部署策略、故障处置的 3 个问题。

#### 交付物清单
`docs/team/charter-v1.0.md`、`docs/team/standup-log.md`、`docs/team/risk-log.md`、`docs/retro/sprint-1..4.md`、`.github/workflows/ci.yml`、`.github/workflows/cd.yml`、`deploy/docker-compose.yml`、`deploy/docker-compose.dev.yml`、`deploy/nginx.conf`、`backend/Dockerfile`、`frontend/Dockerfile`、`docs/ops/runbook-v1.0.md`、Grafana 截图、CI/CD 运行记录截图。

#### 考核关联
团队过程性表现 30%（看板完成度、Daily Standup 参与、回顾会质量、流程纪律）几乎全部由 B 直接承担；Sprint 增量演示"稳定性"；Lab 8 评分（流水线完整性 30% + 部署成功率 25%）。

#### 自查清单
- 看板今天有更新吗？In Progress 是否超 WIP？
- 有没有超过 24 小时无人认领的阻塞卡？是否已升级？
- 每个 PR 是否都经过了 CI 才能合并？
- Secrets 是否全部走环境变量 / GitHub Secrets，无明文入库？

---

### 4.3 成员 C —— 后端领域负责人（Identity + Q&A 子域）

#### 职责定位
领域建模与核心聚合的第一责任人；`backend/pom.xml`、`co-bootstrap`、`co-shared-kernel`、`co-identity`、`co-qa` 的 Owner（附录 F.3）。同时是 L7–L8 DDD 理论的落地执行者。

#### 全程任务

- 主持 Event Storming，产出限界上下文与聚合设计；
- 主导 Spring Boot + Maven 多模块骨架；
- 实现 User / Question / Answer / Tag 聚合，保护不变式（禁止贫血模型）；
- 与 D 协商领域事件契约（如 `AnswerAccepted` → 声誉子域）；
- 性能优化与索引设计。

#### Sprint 0（W1–W2）

1. 完成个人技能矩阵自评，主动承担"Spring Boot / DDD"技术选型说明；
2. 产出技术故事（如"作为开发者，我希望多模块工程可启动"）供 A 排入 Backlog；
3. 在 Lab 1 中完成 hello 文档 PR，示范规范提交信息。

#### Sprint 1（W3–W6）· Lab 3 主责

1. **Big Picture 事件风暴（40 min）**：橙色便利贴罗列所有领域事件，**必须用过去时业务术语**（`QuestionPosted`、`AnswerAccepted`），拒绝"用户操作完成"这类抽象事件；
2. **Process Modeling（30 min）**：为每个事件补蓝色"命令"（谁触发）、黄色"角色"卡片、粉色"外部系统"卡片（邮件服务、OAuth 提供商）；
3. **Software Design 与聚合识别（30 min）**：命令-事件聚类识别聚合（紫色），验证聚合不变式与一致性边界，识别需要的读模型；
4. **上下文映射与术语表（20 min）**：画限界上下文边界，标注上下文间关系（伙伴 / 客户-供应商 / ACL / 共享内核），产出《统一语言术语表 v1.0》≥ 30 个术语（中英对照，提交 `docs/domain/glossary-v1.0.md`）；
5. 产出 `docs/domain/context-map.png`、`docs/domain/aggregates.md`（聚合名、根实体、关键不变式）；
6. 编码落地 Sprint 1 承诺：学号/工号登录、邮箱验证、基于角色的访问控制（学生 / 助教 / 教师 / 管理员）、`POST /api/v1/questions`；
7. **陷阱规避**：上下文不得按技术划分（"数据库上下文""前端上下文"一律否决），必须按业务能力划分。

#### Sprint 2（W7–W10）· Lab 4 主责

1. **Maven 多模块骨架（25 min）**：父 POM 统一版本（Spring Boot 3.x、JDK 21、JUnit 5）；建立 `co-bootstrap`、`co-shared-kernel`、`co-identity`、`co-qa`、`co-reputation`、`co-discovery` 六模块；每模块内部分 `domain / application / infrastructure / interface` 四层；`co-bootstrap` 装配各 context 并声明启动入口；
2. **核心聚合实现（35 min）**：`co-qa-domain` 中编写 `Question` 聚合根、`Answer` 实体、`Vote` 值对象；**所有 Setter 私有化**，通过工厂方法或聚合方法保护不变式；`QuestionRepository` 接口定义在 domain 层、实现在 infrastructure 层；提交首批单元测试；
3. **基础设施层（35 min）**：确定 JPA Entity 与领域实体的映射方案（同一类共用 vs 独立映射），在 ADR 中记录理由；Flyway `V1__init_schema.sql` 创建 `question`、`answer`、`user` 表；`@RestControllerAdvice` 全局异常处理；Logback JSON 结构化日志；
4. **端到端贯通（15 min）**：`QuestionController.create()` → `QuestionApplicationService` → Repository → DB，用 Postman / HTTPie 验证并检查数据库实际写入；
5. 产出 `docs/adr/ADR-002-package-structure.md`（分模块决策）与 Postman Collection。

#### Sprint 3（W11–W13）· Lab 6 参与

1. 实现采纳最佳答案（`AcceptAnswer` 命令）、标签系统（课程标签 / 技术标签 / 自定义标签）；
2. 发布领域事件 `AnswerAccepted`、`QuestionPosted`，通过事件影响声誉子域（**验证多模块协作**）；
3. 配合 D 排查 1–2 个跨模块协作 BUG，沉淀到 `docs/team/known-issues.md`；
4. 补齐核心聚合单测：覆盖率 ≥ 70%，核心聚合 ≥ 90%。

#### Sprint 4（W14–W15）

1. 性能优化：排查 N+1 查询、为热点查询加索引、热点问题加 Redis 缓存并明确失效策略（文档 D.5 评审项）；
2. 修复 CI 中与后端构建相关的失败；
3. 与 B 一起完成容器内启动验证（`application.yml` 通过环境变量注入数据库连接）。

#### 答辩周（W16）

1. 负责"后端 DDD 分层与聚合设计"的技术深度讲解；
2. 独立回答评委关于限界上下文划分、聚合边界、不变式保护的 3 个问题。

#### 交付物清单
`docs/domain/context-map.png`、`docs/domain/aggregates.md`、`docs/domain/glossary-v1.0.md`、`docs/adr/ADR-001`、`docs/adr/ADR-002`、`backend/pom.xml` 及 `co-*` 六个模块全部代码、Flyway 迁移脚本、Postman Collection、后端单元测试。

#### 考核关联
个人代码贡献 15%（git blame 归属 + JaCoCo 个人覆盖率）、架构设计 20%（DDD 分层清晰且有 ADR 支撑）、Lab 4 评分（DDD 分层 30% + 不变式保护 25%）。

#### 自查清单
- 聚合内有没有 public setter？有没有贫血模型？
- 领域事件命名是否使用过去时业务语言？
- 每次 schema 变更是否都有 Flyway 版本脚本，而不是手工改库？
- 涉及架构决策的改动，ADR 写了吗？

---

### 4.4 成员 D —— 后端实现工程师（Reputation + Discovery 子域）

#### 职责定位
互动、声誉、发现三大子域的落地人；`co-reputation`、`co-discovery` 与 `docs/api/openapi.yaml` 的 Owner（附录 F.3）。

#### 全程任务

- 实现投票、评论、声誉计算、徽章、排行榜、悬赏；
- 实现筛选、搜索、相关推荐、通知中心；
- 维护 OpenAPI 契约（契约优先，先改 yaml 再改代码）；
- 与 C 对接领域事件；
- 缓存与查询性能。

#### Sprint 0（W1–W2）

1. 技能矩阵自评，认领"JPA / Redis / 查询优化"方向；
2. 在 Lab 1 完成 hello 文档 PR，熟悉 PR 流程；
3. 参与 Lab 2 生成技术类故事（如"作为学生，我希望看到周榜以便知道谁最活跃"）。

#### Sprint 1（W3–W6）

1. 参与 Lab 3 事件风暴的 Process Modeling 环节，负责"命令"与"外部系统"卡片（邮件服务、OAuth 提供商）；
2. 实现回答提交接口 `POST /api/v1/questions/{id}/answers` 与回答列表查询；
3. 起草 `docs/api/openapi.yaml` 骨架（auth、questions、answers 三组接口）。

#### Sprint 2（W7–W10）· Lab 4 参与

1. 在 `co-reputation-domain` 中实现 `Reputation` 聚合：规则为**回答被采纳 +15、被赞 +10、被踩 -2**；
2. 在 `co-reputation-infrastructure` 中实现 `ReputationRepository` 与事件监听器；
3. 在 `co-discovery` 中实现按课程 / 标签 / 时间筛选的问题列表查询（读模型）；
4. 完成 OpenAPI 契约 v1（`openapi.yaml`）并让 CI 契约校验通过；
5. 与 C 联调"提问 → 回答 → 采纳 → 声誉变化"链路。

#### Sprint 3（W11–W13）· Lab 6 参与

1. 实现投票（赞/踩，防重复投票）、评论与二级回复；
2. 实现徽章体系（回答数、采纳率、连续活跃天数）与周 / 月 / 学期排行榜；
3. 实现基础全文搜索（数据库 LIKE / 全文索引）与基于标签共现的相关问题推荐（初版规则可简化）；
4. 实现通知中心（回答提醒、采纳提醒、@提醒），写库 + 前端轮询/SSE 均可，需在 ADR 中说明取舍；
5. 实现"教师悬赏"：教师用积分悬赏特定问题以吸引高质量回答；
6. 编码纪律：**所有新代码必须先写单元测试**；每完成一个 Task 即提 PR 走评审。

#### Sprint 4（W14–W15）

1. Redis 缓存接入：会话存储、热点问题缓存、限流计数器，并写清缓存失效策略；
2. 查询性能优化：N+1 排查、慢查询日志分析、必要索引补充；
3. 参与"系统上线"：验证测试环境数据与服务健康。

#### 答辩周（W16）

1. 负责"声誉与发现子域设计 + 契约优先实践"的讲解；
2. 独立回答评委关于声誉规则一致性、缓存一致性、搜索演进（LIKE → Elasticsearch）的 3 个问题。

#### 交付物清单
`backend/co-reputation/**`、`backend/co-discovery/**`、`docs/api/openapi.yaml`、通知与搜索相关 ADR、Postman Collection（声誉/搜索用例）、后端单元与集成测试。

#### 考核关联
个人代码贡献 15%、Sprint 增量演示（功能完整性 30%，投票/采纳/声誉为主线）、Lab 6 评分（Sprint Backlog 完成率 30%）。

#### 自查清单
- 声誉规则 (+15 / +10 / -2) 在代码里只有一个来源吗？有没有散落硬编码？
- 投票是否可重复？并发下是否幂等？
- OpenAPI 契约是否先于实现修改？
- 通知失败是否会阻断主流程？（应降级而非抛错）

---

### 4.5 成员 E —— 前端工程师（Q&A / Reputation 前端）+ QA

#### 职责定位
前端工程化基建的 Owner（`frontend/src/shared`、`features/qa`、`features/reputation`、`src/api`），同时承担 QA 职责（附录 F.4）：编写并维护集成测试与端到端测试、设计 Sprint 验收测试场景、运行回归测试、发布前给出 Go/No-Go 建议、沉淀典型缺陷案例。

#### 全程任务

- 前端脚手架、代码质量门禁（ESLint / Prettier / Husky）；
- API 客户端由 OpenAPI 自动生成，禁止手写类型；
- 测试规范与测试报告；
- 组织跨成员代码评审（Lab 7 要求 ≥ 4 次）。

#### Sprint 0（W1–W2）

1. 技能矩阵自评，认领"前端工程化 + 测试"方向；
2. 完成 hello 文档 PR；
3. 参与《团队章程》中 DoD 初版的撰写（**由 E 提供"可测性"条款**）。

#### Sprint 1（W3–W6）

1. 与 A 合作为 Sprint 1 每条故事写 Gherkin 验收标准，**并定义每条场景的自动化可行性**（可行 / 手工）；
2. 产出前端技术选型说明：React 18 + TS 5、Vite、Zustand、TanStack Query、Ant Design 5 / Tailwind；
3. 搭建测试目录结构与 CI 前端 Job 骨架（`pnpm lint && pnpm typecheck && pnpm test`）。

#### Sprint 2（W7–W10）· Lab 5 主责

1. **工程初始化（20 min）**：`pnpm create vite@latest` 选 react-ts；启用 `strict` 与 `noUncheckedIndexedAccess`；安装 `react-router-dom` v6、`zustand`、`@tanstack/react-query`、`axios`、`antd`；建立 `features/`、`shared/`、`pages/`、`api/` 目录；
2. **代码质量基建（20 min）**：ESLint + `@typescript-eslint` + `eslint-plugin-react-hooks`；Prettier + EditorConfig；Husky + lint-staged pre-commit；`vite.config.ts` 配置 `@/` 路径别名；
3. **API 客户端（30 min）**：基于 `openapi.yaml` 用 `openapi-typescript-codegen` 生成 TS 类型与请求函数；封装 axios 拦截器（Token 注入、统一错误处理、全局 Loading）；配置 `useQueryClient` 与 `ErrorBoundary`；
4. **首页与提问页（40 min）**：AppShell 布局（Header + Sidebar + Outlet）；路由 `/questions`、`/questions/new`、`/questions/:id`；用函数组件 + Hooks 实现 `QuestionListPage`、`NewQuestionPage`；用 TanStack Query 做请求与缓存；提交成功后跳转详情页；
5. **陷阱规避**：严禁 `useState` 到处散落造成 prop drilling（严格区分服务器状态 = TanStack Query 与客户端状态 = Zustand）；严禁 `any` 逃逸（CI 加 `tsc --noEmit`）；
6. 验收：`pnpm dev` 可启动、`pnpm lint` 与 `pnpm typecheck` 全绿、pre-commit 能拦截不合规提交、提问→列表→详情端到端跑通并录屏 ≤ 30 秒。

#### Sprint 3（W11–W13）· Lab 7 主责（与 B 配合）

1. **单元测试集中编写（40 min）**：识别覆盖率盲点（JaCoCo 报告）；优先补齐核心领域逻辑；使用 `ArgumentCaptor` 验证副作用调用；清理"无效断言"测试用例；
2. **集成测试与契约测试（25 min）**：用 Testcontainers 编写 1–2 个覆盖 `Controller → Service → Repository` 的完整集成测试；用 Pact 编写消费者驱动契约测试（可选，加分项）；
3. **质量门禁（20 min）**：配合 B 将 JaCoCo 与 Sonar 检查作为 CI 的必经步骤，覆盖率门禁 ≥ 70%；
4. **代码评审实战（35 min）**：为每位成员指定 1 个**自己未参与**的 PR 做深度评审；使用课程评审清单（正确性、可读性、可测试性、安全、性能、风格）；每条 PR 至少 5 条实质性意见；汇总《团队代码评审清单 v1.0》到 `docs/team/review-checklist.md`；
5. **前端测试**：用 Vitest 编写组件与 hooks 单测；用 Playwright 编写核心链路（登录 → 提问 → 回答 → 采纳）E2E；
6. 产出测试报告 `docs/qa/test-report-sprint3.md`：覆盖率、缺陷清单、遗留风险。

#### Sprint 4（W14–W15）· Lab 8 参与

1. 上线后执行完整回归测试（含移动端视口基本可用性）；
2. 验证告警真实触发（人为造 5xx 或高延迟，确认告警到达团队 IM）；
3. 发布前给出 **Go / No-Go 建议**（依据：E2E 全绿、无 P0/P1 缺陷、Smoke Test 通过）；
4. 沉淀典型缺陷案例 `docs/qa/defect-cases.md`（≥ 5 例：现象 / 根因 / 修复 / 如何预防）。

#### 答辩周（W16）

1. 负责"前端工程化与测试策略"的讲解片段；
2. 独立回答评委关于测试金字塔、E2E 与单测取舍、类型安全实践的 3 个问题。

#### 交付物清单
`frontend/` 全部工程配置（package.json、vite.config.ts、eslint、prettier、husky）、`src/api/**` 生成客户端、`docs/qa/test-report-sprint3.md`、`docs/qa/defect-cases.md`、`docs/team/review-checklist.md`、PR 评审记录（≥ 4 份，可导出 PDF）、JaCoCo 覆盖率报告 HTML、SonarQube Dashboard 截图。

#### 考核关联
Lab 7 评分（覆盖率 30% + 质量门禁 25% + 评审意见质量 25% + 清单适用性 20%）、Lab 5 评分（TS 严格模式 25% + Hooks 范式 25% + 端到端 30%）、个人代码贡献 15%。

#### 自查清单
- 前端是否存在 `any`？`tsc --noEmit` 是否全绿？
- 新功能是否同时补了单测与（必要时）E2E？
- 今天评审的 PR 是否给出了可操作的改进建议，而不是"看起来不错"？
- 发布前 Go/No-Go 结论是否有数据支撑？

---

## 5. Sprint 任务分解（WBS）

> 每个 Sprint 的卡片可直接复制到看板。责任人代号同第 1 章。完成标准一律引用第 2.4 节 DoD。

### Sprint 0 · W1–W2 · 启动与建模

**Sprint Goal**：团队成型、协作基础设施可用、产品愿景与 Backlog 初版就位。

| 编号 | 任务 | 负责 | 协作 | 产出 | 完成标准 |
| --- | --- | --- | --- | --- | --- |
| S0-1 | 技能矩阵自评 + 组队 + 角色承诺书 | 全员 | A | `docs/team/role-commitment.md` | 4–6 人组队完成，PO/SM 产生并签字 |
| S0-2 | Fork 模板仓库、分支保护、看板、IM | B | 全员 | 仓库 URL + 看板截图 | main 禁直推、PR 强制评审、看板 ≥ 5 条初始 Backlog |
| S0-3 | 首次 Git 协作演练 | 全员 | B | `docs/team/hello-<name>.md` × N | main 包含全体成员 hello 文档 |
| S0-4 | 《团队章程 v1.0》 | B 起草 | 全员 | `docs/team/charter-v1.0.md` | 含价值观、沟通约定、开发规约、DoD 初版，全员签名 |
| S0-5 | 产品愿景声明 + 竞品分析 | A | E | `docs/backlog/vision.md` | 说清为谁/解决什么/成功标准 |
| S0-6 | 技术栈确认与环境安装 | C、D、E | B | 环境检查表 | JDK21 / Node20 / Docker 全部可用 |

**关键步骤（Lab 1 现场 90 分钟节奏）**

1. Step 1 团队组建与角色确认（30 min）→ 产出 PO/SM/Dev 角色承诺书；
2. Step 2 协作工具链配置（40 min）→ 仓库 + 分支保护 + 看板 + IM；
3. Step 3 Git 协作首次演练（30 min）→ `feature/<姓名拼音>-hello` → PR → LGTM → SM 合并；
4. Step 4 团队章程撰写（20 min）→ 提交 `docs/team/charter-v1.0.md`。

---

### Sprint 1 · W3–W6 · 身份与问答内核

**Sprint Goal**：用户注册登录、发布问题、查看问题列表、提交回答（端到端打通）。

| 编号 | 任务 | 负责 | 协作 | 产出 |
| --- | --- | --- | --- | --- |
| S1-1 | 用户访谈 ≥ 3 份 + 访谈纪要 | A | 全员 | `docs/backlog/interviews.md` |
| S1-2 | 用户故事地图 + Walking Skeleton 切片 | A | 全员 | 故事地图 PNG |
| S1-3 | Product Backlog 精化（INVEST）+ 优先级排序 | A | C、D、E | `docs/backlog/product-backlog.md` |
| S1-4 | Gherkin 验收标准（每条故事 ≥ 3 场景） | A | E | `docs/backlog/stories/*.feature` |
| S1-5 | Sprint 1 Planning + 看板落卡（含 WIP） | A、B | 全员 | `docs/backlog/sprint-1-plan.md` |
| S1-6 | 事件风暴 Big Picture | C | 全员 | 现场全景照片 |
| S1-7 | Process Modeling + 上下文映射 | C、D | 全员 | `docs/domain/context-map.png` |
| S1-8 | 聚合识别与不变式定义 | C | D | `docs/domain/aggregates.md` |
| S1-9 | 统一语言术语表 v1.0（≥ 30 术语） | C | 全员 | `docs/domain/glossary-v1.0.md` |
| S1-10 | 登录 / 注册 / 邮箱验证后端 | C | A | `co-identity` 接口 + 单测 |
| S1-11 | 登录 / 注册前端页面 | A | E | `features/identity` 页面 |
| S1-12 | 发布问题 + 问题列表（后端） | C | D | `POST /api/v1/questions`、`GET /api/v1/questions` |
| S1-13 | 提交回答（后端） | D | C | `POST /api/v1/questions/{id}/answers` |
| S1-14 | OpenAPI 契约骨架 | D | E | `docs/api/openapi.yaml` |
| S1-15 | Sprint 1 Review + Retro | A、B | 全员 | `docs/review/sprint-1-review.md`、`docs/retro/sprint-1.md` |

**关键步骤**

1. 周一 Planning：PO 讲 Sprint Goal → 团队认领故事 → 拆任务 → 落看板 → 设定 Daily Standup 时间与理想燃尽线；
2. 每周实验课前 10 分钟 Standup（轮换主持），当天卡片状态必须变更；
3. 每完成一个 Task：写测试 → 提 PR → 交叉评审 → CI 绿 → Squash 合并 → 卡片入 Done；
4. W6 Review：以"注册 → 提问 → 列表 → 回答"闭环演示，利益相关者打分；
5. W6 次日 Retro：Start / Stop / Continue，匿名贴 3 张卡片，投票选出 2–3 项改进进入 Sprint 2。

---

### Sprint 2 · W7–W10 · 互动与声誉

**Sprint Goal**：投票、采纳、评论、标签、声誉计算、个人主页可用。

| 编号 | 任务 | 负责 | 协作 | 产出 |
| --- | --- | --- | --- | --- |
| S2-1 | Maven 多模块骨架（六模块 + 四层） | C | D、B | 可 `mvn clean install` 的工程 |
| S2-2 | `Question`/`Answer`/`Vote` 聚合实现 + 单测 | C | D | `co-qa-domain` |
| S2-3 | Flyway `V1__init_schema.sql` + JPA 映射 | C | D | 迁移脚本 |
| S2-4 | 全局异常处理 + Logback JSON 日志 | C | B | `@RestControllerAdvice` |
| S2-5 | 首个端到端接口贯通 + Postman Collection | C | A | `POST /api/v1/questions` 成功用例 |
| S2-6 | ADR-002 包结构与分模块决策 | C | 全员 | `docs/adr/ADR-002-package-structure.md` |
| S2-7 | 前端工程初始化 + 质量基建 + Husky | E | A | `frontend/` 工程 |
| S2-8 | API 客户端生成 + axios 拦截器 + ErrorBoundary | E | D | `src/api/**` |
| S2-9 | 问题列表页 / 详情页 / 提问页 | A、E | C | `pages/**` |
| S2-10 | `Reputation` 聚合（+15/+10/-2）+ 事件监听 | D | C | `co-reputation` |
| S2-11 | 筛选查询读模型 | D | A | `co-discovery` |
| S2-12 | OpenAPI 契约 v1 + CI 契约校验 | D、B | E | `openapi.yaml` + CI 步骤 |
| S2-13 | 个人主页（声誉值 / 徽章展示） | A | D | `pages/ProfilePage` |
| S2-14 | 本地依赖环境（MySQL + Redis）compose | B | C、D | `deploy/docker-compose.dev.yml` |
| S2-15 | Sprint 2 Review（重点演示投票→声誉链路） | A | 全员 | 评审纪要 |

**关键步骤**

1. W7 Lab 4 现场 4 步：Maven 骨架（25）→ 聚合实现（35）→ 基础设施（35）→ 端到端接口（15）；
2. W9 Lab 5 现场 4 步：工程初始化（20）→ 质量基建（20）→ API 客户端（30）→ 首页与提问页（40）；
3. 前后端以 `openapi.yaml` 为唯一契约入口：**先改契约、再改代码**，CI 校验不通过不得合并；
4. W10 Review 演示"投票 → 声誉值变化 → 个人主页徽章更新"的完整可观测链路。

---

### Sprint 3 · W11–W13 · 发现与通知

**Sprint Goal**：搜索、筛选、相关推荐、通知中心、悬赏机制上线；质量门禁生效。

| 编号 | 任务 | 负责 | 协作 | 产出 |
| --- | --- | --- | --- | --- |
| S3-1 | 投票（赞/踩，防重复）+ 评论与二级回复 | D | E | `co-reputation`、`co-qa` |
| S3-2 | 采纳最佳答案 + 标签系统 | C | A | 采纳 / 标签接口 |
| S3-3 | 领域事件贯通（Identity/QA → Reputation） | C、D | — | 事件契约文档 |
| S3-4 | 徽章体系 + 周/月/学期排行榜 | D | A | 排行榜接口 + 页面 |
| S3-5 | 基础全文搜索（LIKE / 全文索引） | D | A | 搜索接口 |
| S3-6 | 相关问题推荐（标签共现） | D | A | 推荐接口 |
| S3-7 | 通知中心（回答 / 采纳 / @提醒） | D | A、E | 通知接口 + 页面 |
| S3-8 | 教师悬赏（积分悬赏 + 结算规则） | D | A | 悬赏模块 |
| S3-9 | 前端：搜索 / 筛选 / 通知中心页面 | A、E | D | `pages/**` |
| S3-10 | 单元测试覆盖率补齐至 ≥ 70%（核心聚合 ≥ 90%） | C、D | E | JaCoCo 报告 |
| S3-11 | SonarQube + JaCoCo + Checkstyle 质量门禁接入 CI | B、E | 全员 | CI 配置 + Dashboard 截图 |
| S3-12 | 跨成员 PR 深度评审 ≥ 4 次 + 评审清单 v1.0 | E | 全员 | `docs/team/review-checklist.md` |
| S3-13 | 契约测试（Pact，可选）+ Playwright E2E 核心链路 | E | D | 测试代码 |
| S3-14 | 测试报告（覆盖率 / 缺陷 / 遗留风险） | E | 全员 | `docs/qa/test-report-sprint3.md` |
| S3-15 | Sprint 3 Review（每队 5 分钟）+ Retro | A、B | 全员 | 纪要、ADR ≥ 1 份 |

**关键步骤（Lab 6 现场）**

1. Step 1 编码冲刺（60 min）：严守 Sprint Backlog，**不接受范围蔓延**；所有新代码先写单测；每完成一个 Task 即 PR；
2. Step 2 Sprint Review（35 min，每队 5 min）：PO 回顾目标 → Developer 现场演示 → 利益相关者提问打分 → PO 决定哪些反馈进 Sprint 4 Backlog；
3. Step 3 Retro（25 min）：Sailboat / Start-Stop-Continue，每人匿名 3 张卡片，投票选出 2–3 项改进。

**关键步骤（Lab 7 现场）**

1. 单元测试集中编写（40 min）→ 识别盲点、补核心领域、`ArgumentCaptor` 验证副作用、删"无效断言"；
2. 集成与契约测试（25 min）→ Testcontainers 全链路 1–2 条；
3. 质量门禁（20 min）→ JaCoCo + Sonar + Checkstyle 接入 CI；
4. 评审实战（35 min）→ 每人评审 1 个非自己参与的 PR，≥ 5 条实质意见。

---

### Sprint 4 · W14–W15 · 质量与部署

**Sprint Goal**：CI/CD 流水线、Docker 部署上线、监控告警、性能优化全部完成，系统在校园网内可访问。

| 编号 | 任务 | 负责 | 协作 | 产出 |
| --- | --- | --- | --- | --- |
| S4-1 | 后端多阶段 Dockerfile（jlink / distroless，非 root） | B | C | `backend/Dockerfile` |
| S4-2 | 前端镜像 + Nginx 反代 + SPA fallback | B | E | `frontend/Dockerfile`、`deploy/nginx.conf` |
| S4-3 | docker compose 全栈编排 + 健康检查 + .env | B | 全员 | `deploy/docker-compose.yml` |
| S4-4 | CI/CD 扩展：build → push → SSH 部署 → Smoke Test | B | E | `.github/workflows/cd.yml` |
| S4-5 | Prometheus + Grafana（≥ 5 核心指标）+ ≥ 1 告警 | B | C、D | Dashboard 截图 + 告警规则 |
| S4-6 | 性能优化：N+1、索引、热点缓存与失效策略 | C、D | B | 优化前后对比数据 |
| S4-7 | 上线后回归测试 + Go/No-Go 建议 | E | 全员 | `docs/qa/go-no-go.md` |
| S4-8 | 典型缺陷案例沉淀（≥ 5 例） | E | 全员 | `docs/qa/defect-cases.md` |
| S4-9 | 运维手册 v1.0（启动/停止/回滚/故障处置） | B | 全员 | `docs/ops/runbook-v1.0.md` |
| S4-10 | 上线通告 + 友测邀请 + 外部反馈归档 | A | B | 反馈记录 |
| S4-11 | Demo 脚本与答辩彩排（≥ 1 次全流程彩排） | A | 全员 | Demo 脚本 |

**关键步骤（Lab 8 现场）**

1. 后端镜像（20 min）→ 多阶段构建 + 体积优化 + 非 root；
2. 前端镜像与 Nginx（15 min）→ `/api` 反代 + SPA fallback；
3. compose 编排（20 min）→ app + MySQL + Redis + nginx + Prometheus + Grafana，`service_healthy` 依赖顺序；
4. CI/CD 扩展（30 min）→ build/test/sonar 后追加 build → push → deploy → smoke；
5. 监控与上线验证（15 min）→ Grafana 指标正常 → 配置告警 → IM 群通告 URL → 邀请友测。

---

### 答辩周 · W16 · 交付与回顾

| 环节 | 时长 | 主责 | 要求 |
| --- | --- | --- | --- |
| 系统演示 | 8 min | A | 完整业务闭环，PO 主导 |
| 技术深度讲解 | 7 min | C（后端）/ E（前端）/ B（DevOps） | 分人讲解关键设计决策 |
| 评委个人提问 | 12 min | 全员 | **每人被随机抽问 3 个**，围绕各自负责模块的设计、选型、踩坑 |
| 开放讨论 | 3 min | A | 团队反思与未来改进展望 |

**答辩前 3 天检查清单**

- 系统在课程统一部署环境可访问，且有真实流量数据；
- 每位成员都能独立讲清自己负责模块的**设计意图**（红线要求，AI 生成的代码也必须能讲）；
- SonarQube 重复率检查通过，无跨团队复制；
- 个人反思日志与随堂测验已提交（个人理论与反思 10%）；
- 代码与文档归档完成（README、ADR、运维手册、测试报告齐全）。

---

## 6. 实验课（Lab 1–8）分工落位表

| Lab | 周次 | Sprint | 主责人 | 现场分工 |
| --- | --- | --- | --- | --- |
| Lab 1 团队组建与协作工具链 | W2 | S0 | B | B：仓库/分支保护/看板；A：章程价值观章节；C：技术规约；D、E：Git 演练 |
| Lab 2 需求建模工作坊 | W3 | S1 | A | A：故事地图主持；E：Gherkin；C、D：技术故事与估点 |
| Lab 3 DDD 事件风暴 | W5 | S1 | C | C：Big Picture 主持；D：Process Modeling；B：记录与风险登记；A、E：角色与外部系统卡片 |
| Lab 4 后端工程骨架 | W7 | S2 | C | C：Maven 骨架 + 聚合；D：Flyway + Repository；B：本地 DB 容器 |
| Lab 5 前端工程骨架与对接 | W9 | S2 | E | E：工程与质量基建 + API 客户端；A：页面；C、D：接口联调 |
| Lab 6 核心特性端到端开发与 Review | W11 | S3 | A + D | D：投票/评论/声誉编码；A：Review 主持；B：环境保障；E：即时验证 |
| Lab 7 测试与代码评审实战 | W13 | S3 | E + B | E：测试与评审组织；B：Sonar / JaCoCo 门禁；C、D：覆盖率补齐 |
| Lab 8 上线部署与监控 | W15 | S4 | B | B：镜像 / compose / CI-CD / 监控；C、D：性能；E：回归与 Go/No-Go；A：上线通告 |

---

## 7. 考核映射：团队成绩 → 个人成绩

### 7.1 成绩构成（文档 7.2）

| 考核项 | 权重 | 评价单位 | 主要承担人 | 每位成员的关键动作 |
| --- | --- | --- | --- | --- |
| 团队过程性表现 | 30% | 团队 | B（流程）/ A（价值） | 看板实时更新、Standup 全员参与、Retro 有实质改进项、文档规范 |
| Sprint 增量演示 | 25% | 团队 | A（演示）/ 全员 | 4 次 Review 按 Rubric 评分（功能 / 稳定性 / 设计 / 答辩） |
| 个人代码贡献 | 15% | 个人 | 全员 | Git 提交频次与质量、PR 数量与评审质量、git blame 归属、JaCoCo 个人覆盖率 |
| 期末团队答辩 | 20% | 团队 + 个人 | 全员 | 终极演示 + **每人 5 分钟独立技术问答** |
| 个人理论与反思 | 10% | 个人 | 全员 | 随堂测验 5% + 个人反思日志 5% |
| 加分项 | +5% | 团队/个人 | 自愿 | 开源贡献、技术博客、附加功能、跨团队帮助（教师裁量，封顶 +5%） |

### 7.2 Sprint Review 评分量规（文档 7.4）

| 维度 | 权重 | 优秀（90–100） | 良好（75–89） | 需改进（<75） |
| --- | --- | --- | --- | --- |
| 功能完整性 | 30% | 承诺故事 100% 完成，演示流畅 | 80% 以上完成，1–2 处小瑕疵 | 少于 80% 或存在阻塞性 BUG |
| 代码质量 | 25% | Sonar Coverage > 80% 且 0 Bug | Coverage 60–80%，Bug < 3 | 低于 60% 或存在严重 Smell |
| 架构设计 | 20% | 清晰的 DDD 分层，有 ADR 支撑 | 基本符合分层，ADR 完整 | 出现严重违反 SOLID 或层级混乱 |
| 流程纪律 | 15% | 看板实时更新，Standup 全员参与 | 基本规范，偶有遗漏 | 看板长期未更新或角色失位 |
| 团队答辩 | 10% | 应答有深度，展现技术理解 | 基本回答正确 | 回答含糊或暴露设计缺失 |

### 7.3 防"搭便车"三条规则（文档 7.3）

1. **贡献度系数**：每个 Sprint 末匿名互评，产出 0.7–1.3 的系数；教师结合 Git 数据、PR 质量、看板任务完成情况定稿。个人团队部分得分 = 团队得分 × 贡献度系数；
2. **60% 门槛**：Git 提交、PR、评审参与度三项中任意两项低于团队平均的 40%，该 Sprint 团队成绩按 60% 折扣，且需单独完成补足任务；
3. **答辩个人化**：每人必须独立回答与自己负责模块相关的 3 个技术问题，个人得分 ≠ 团队演示得分。

### 7.4 各成员最需要守住的指标

| 成员 | 高风险项 | 应对 |
| --- | --- | --- |
| A | 只做"记录"不做"决策"，被判定 PO 失位 | 每个 Sprint 必须有明确的优先级取舍记录 |
| B | 只做工具不做流程，看板长期不更新 | 每日 22:00 前完成看板同步，Standup 纪要当日提交 |
| C | 架构决策无 ADR，被扣"架构设计 20%" | 每个跨模块决策落 1 份 ADR |
| D | 代码量大但评审参与少，触发 60% 门槛 | 每周至少评审 2 个 PR |
| E | 测试只覆盖前端，后端覆盖率不达标 | 覆盖率门禁进 CI，未过不得合并 |

---

## 8. 风险清单与常见陷阱

| 风险 / 陷阱 | 触发信号 | 预防措施 | 责任人 |
| --- | --- | --- | --- |
| 成员各自在 main 直推 | 出现无 PR 的提交 | Lab 1 第一步配置分支保护 | B |
| 角色只是名义，SM 不主持会议 | Standup 无纪要 | 本周末 SM 主持首次 Standup，提交纪要 | B |
| 事件粒度过粗（"用户操作完成"） | 事件无法命名业务含义 | 坚持业务术语 + 过去时命名 | C |
| 上下文按技术划分 | 出现"数据库上下文" | 严格按业务能力划分，教师当场纠正 | C |
| 聚合贫血、Setter 公开 | 领域类只有 getter / setter | 私有 Setter + 工厂方法 + 单测保护不变式 | C |
| `any` 类型逃逸 | TS 失去类型价值 | CI 加 `tsc --noEmit` | E |
| useState 散落导致 prop drilling | 组件间层层透传 | 服务器状态用 TanStack Query，客户端状态用 Zustand | E |
| N+1 查询 | 详情页响应随数据量劣化 | 评审清单 D.5 强制检查 + 慢查询日志 | D |
| 缓存失效策略缺失 | 数据更新后页面不刷新 | 写清 TTL 与主动失效，写入 ADR | D |
| Dockerfile 硬编码密码 | 仓库出现明文密码 | 环境变量 + GitHub Secrets | B |
| 容器跨主机网络不通 | 容器互相访问失败 | 统一同一 Docker network 编排 | B |
| 部署后 404 | 刷新子路由报错 | Nginx + React Router fallback 先在本地验证 | B、E |
| 范围蔓延 | Sprint 中途加入新故事 | PO 拒绝：新需求一律进下个 Sprint Backlog | A |
| CI 长时间红灯 | 多次提交无人修复 | 10 分钟反馈原则，红灯优先修复 | B |
| 文档"水"化 / AI 直接生成 | 答辩讲不清设计意图 | 学术诚信红线：讲不清即视为抄袭 | 全员 |

---

## 9. 附录：交付物总清单与验收入口

| 类别 | 文件 / 产物 | 责任人 | 时间 |
| --- | --- | --- | --- |
| 团队治理 | `docs/team/charter-v1.0.md`、`role-commitment.md`、`standup-log.md`、`risk-log.md`、`review-checklist.md` | B | S0–S3 |
| 需求 | `docs/backlog/vision.md`、`product-backlog.md`、`stories/*.feature`、`rules-reputation.md`、故事地图 PNG、`sprint-N-plan.md` | A | S0–S4 |
| 领域建模 | `docs/domain/context-map.png`、`aggregates.md`、`glossary-v1.0.md` | C | S1 |
| 架构决策 | `docs/adr/ADR-001..00N.md` | 决策提出人 | 持续 |
| 接口契约 | `docs/api/openapi.yaml` | D | S1–S4 |
| 后端工程 | `backend/` 六个模块 + Flyway 脚本 + Postman Collection | C、D | S2–S4 |
| 前端工程 | `frontend/` 工程 + 生成客户端 + 页面 | A、E | S2–S4 |
| 测试与质量 | JaCoCo HTML 报告、SonarQube Dashboard 截图、`docs/qa/test-report-sprint3.md`、`defect-cases.md`、`go-no-go.md`、PR 评审记录（≥ 4 份） | E | S3–S4 |
| DevOps | `.github/workflows/*.yml`、`deploy/docker-compose.yml`、`nginx.conf`、`Dockerfile` × 2、`docs/ops/runbook-v1.0.md`、Grafana 截图 | B | S4 |
| 过程记录 | `docs/review/sprint-N-review.md`、`docs/retro/sprint-N.md` | A、B | 每 Sprint |
| 个人 | 读书笔记、反思日志、随堂测验、答辩个人问答准备 | 全员 | 全程 |

### 交付物验收入口（对应 DoD 检查点）

1. `mvn -q clean verify` 全绿；
2. `pnpm lint && pnpm typecheck && pnpm test` 全绿；
3. CI 流水线徽章绿色（构建 / 测试 / 静态分析 / 安全扫描 / 质量门禁）；
4. 至少 1 名队友 LGTM 的 PR 记录；
5. Gherkin 验收标准全部通过；
6. 测试环境 Smoke Test 通过（`/actuator/health`）；
7. `openapi.yaml` 与实际接口一致；
8. 涉及架构决策的 ADR 已提交；
9. README 与用户文档已更新。

---

*本手册与《CampusOverflow 软件工程教学方案》同步迭代；若教学方案更新，请以方案原文为准并同步修订本手册对应章节。*
