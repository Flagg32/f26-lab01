# 团队角色承诺书

> 提交人：各成员（签字以 commit message 形式完成）    生效日期：2026-10-10    团队规模：5 人

## 一、角色分配（5 人团队）

| 代号 | 姓名 | 角色 | 兼任 | 技术主场 |
| --- | --- | --- | --- | --- |
| A | 唐志烽 | Product Owner（PO） | 前端 Dev（Identity / Discovery 前端） | 需求分析、React + TS |
| B | 左焱之 | Scrum Master（SM） | DevOps / 部署负责人 | CI/CD、Docker、Prometheus |
| C | 江思达 | Developer | 后端领域负责人（Identity + Q&A 子域） | Spring Boot 3、DDD、JPA |
| D | 何心怡 | Developer | 后端实现（Reputation + Discovery 子域） | JPA、Redis、查询优化 |
| E | 柯博 | Developer | 前端 Dev（Q&A / Reputation 前端）+ QA | React + TS、Vitest、Playwright |

> **本表为建议分工**，W2 Lab 1 现场可调整；调整后请同步更新本表与《CampusOverflow 团队分工方案》。
>
> 边界与纪律（教学方案 L5 明令）：
> - PO 与 SM 必须由两人分任，禁止 PO 兼 SM；
> - SM（左焱之）的技术产出限定在 `deploy/` 与 `.github/workflows/`，避免既当流程主人又当代码 Owner；
> - QA 由 E（柯博）兼任，后端单元 / 集成测试仍由 C（江思达）、D（何心怡）各自负责，E 只做标准制定 + 抽查 + 端到端。

## 二、角色承诺

### A · PO（唐志烽）
1. 维护 Product Backlog，保证每条故事有优先级与验收标准；
2. 主持每次 Sprint Planning，明确 Sprint Goal；
3. 主持每次 Sprint Review，用真实系统演示验收；
4. 拒绝无依据的范围蔓延，并把拒绝理由写进 Backlog 变更记录。

签名：_____________（commit message：`sign(team): 唐志烽 承诺担任 PO`）

### B · SM（左焱之）
1. 主持 Daily Standup 与 Retrospective，当场产出纪要；
2. 每日更新看板，维护 WIP 限制（In Progress ≤ 3、In Review ≤ 3）；
3. 清除障碍，阻塞超过 24 小时升级到教师；
4. 维护 main 分支保护与合并纪律，负责 CI/CD、部署与可观测性。

签名：_____________（commit message：`sign(team): 左焱之 承诺担任 SM`）

### C · Developer / 后端领域负责人（江思达）
1. 主持 Event Storming，产出限界上下文图与聚合设计；
2. 主导 Spring Boot + Maven 多模块骨架与核心聚合（Question / Answer / Vote）实现；
3. 只认领自己能在本 Sprint 完成的故事，每完成一个任务即提 PR；
4. 每周至少评审 2 个他人 PR，对 `co-identity` / `co-qa` 代码质量负责。

签名：_____________（commit message：`sign(team): 江思达 承诺担任 Dev`）

### D · Developer / 后端实现（何心怡）
1. 负责投票、采纳、评论、标签、声誉计算、搜索、通知中心与悬赏的实现；
2. 维护 `docs/api/openapi.yaml` 契约，遵循"先改契约、再改代码"；
3. 负责 `co-reputation` / `co-discovery` 的单元与集成测试；
4. 每周至少评审 2 个他人 PR。

签名：_____________（commit message：`sign(team): 何心怡 承诺担任 Dev`）

### E · Developer / 前端 + QA（柯博）
1. 搭建前端工程与质量门禁（ESLint / Prettier / Husky），维护 `src/api` 自动生成客户端；
2. 承担 QA 职责：维护集成测试与端到端测试、设计 Sprint 验收场景、发布前给出 Go/No-Go 建议；
3. 组织跨成员代码评审（Lab 7 要求 ≥ 4 次），沉淀《团队代码评审清单》；
4. 沉淀典型缺陷案例，维护测试报告。

签名：_____________（commit message：`sign(team): 柯博 承诺担任 Dev + QA`）

## 三、团队共同约定

- 响应时限：工作日消息 4 小时内回复，紧急阻塞 1 小时内；
- 会议纪律：迟到者负责下次会议纪要；无故缺席记入贡献度互评依据；
- 沟通渠道：日常用 IM 群，决策必须落到仓库文档（IM 里的结论不算数）；
- 冲突处理：技术分歧以 ADR 记录并表决，产品分歧以 PO 决定并记录理由；
- 禁止：直接 push main、跨团队复制代码、提交 AI 生成且无法讲解的内容；
- 同步机制：本表与《CampusOverflow 团队分工方案》第 1.1 节保持一致，任一改动需同时更新两处。
