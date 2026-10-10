# 团队角色承诺书

> 提交人：各成员（签字以 commit message 形式完成）    生效日期：<YYYY-MM-DD>    团队规模：4 人

## 一、角色分配（4 人团队）

| 代号 | 姓名 | 角色 | 兼任 | 技术主场 |
| --- | --- | --- | --- | --- |
| A | <你的姓名> | Product Owner（PO） | 前端 Dev（Identity / Discovery） | 需求分析、React + TS |
| B | 柯博 | Scrum Master（SM） | DevOps / 部署 | CI/CD、Docker、监控 |
| C | 江思达 | Developer | 后端领域负责人（Identity + Q&A 子域） | Spring Boot、DDD、JPA |
| D | 何心怡 | Developer | 后端实现（Reputation + Discovery 子域）+ QA | JPA、Redis、Vitest / Playwright |

> **本表为建议分工**，W2 Lab 1 现场可调整；调整后请同步更新本表与《CampusOverflow 团队分工方案》。
>
> 4 人团队按《CampusOverflow 团队分工方案》第 1.2 节的映射执行：
> - Discovery 前端（搜索 / 筛选 / 相关问题推荐）降级为 P2，Sprint 3 内只做「按课程标签筛选 + LIKE 搜索」；
> - QA 职责由 D 承接，原 E 的 Playwright E2E、测试报告与 Go/No-Go 建议并入 D；
> - 后端不再细分「身份与问答」和「声誉与发现」两人，由 C、D 分域承担，跨域改动必须走 ADR + PR 评审。

## 二、角色承诺

### A · 柯博以外的前端与 PO 负责人（PO）
1. 维护 Product Backlog，保证每条故事有优先级与验收标准；
2. 主持每次 Sprint Planning，明确 Sprint Goal；
3. 主持每次 Sprint Review，用真实系统演示验收；
4. 拒绝无依据的范围蔓延，并把拒绝理由写进 Backlog 变更记录。

签名：_____________（commit message：`sign(team): <姓名> 承诺担任 PO`）

### B · 柯博（SM）
1. 主持 Daily Standup 与 Retrospective，当场产出纪要；
2. 每日更新看板，维护 WIP 限制；
3. 清除障碍，阻塞超过 24 小时升级到教师；
4. 维护 main 分支保护与合并纪律。

签名：_____________（commit message：`sign(team): 柯博 承诺担任 SM`）

### C · 江思达（Developer / 后端领域负责人）
1. 主持 Event Storming，产出限界上下文与聚合设计；
2. 主导 Spring Boot + Maven 多模块骨架与核心聚合实现；
3. 只认领自己能在本 Sprint 完成的故事，每完成任务即提 PR；
4. 每周至少评审 2 个他人 PR，并对 `co-identity` / `co-qa` 的代码质量负责。

签名：_____________（commit message：`sign(team): 江思达 承诺担任 Dev`）

### D · 何心怡（Developer / 后端实现 + QA）
1. 负责投票、采纳、评论、标签、声誉计算、搜索与通知的实现；
2. 承担 QA 职责：维护集成测试与端到端测试、设计 Sprint 验收场景、发布前给出 Go/No-Go 建议；
3. 沉淀典型缺陷案例，维护测试报告；
4. 只认领自己能在本 Sprint 完成的故事，每周至少评审 2 个他人 PR。

签名：_____________（commit message：`sign(team): 何心怡 承诺担任 Dev + QA`）

## 三、团队共同约定

- 响应时限：工作日消息 4 小时内回复，紧急阻塞 1 小时内；
- 会议纪律：迟到者负责下次会议纪要；无故缺席记入贡献度互评依据；
- 沟通渠道：日常用 IM 群，决策必须落到仓库文档（IM 里的结论不算数）；
- 冲突处理：技术分歧以 ADR 记录并表决，产品分歧以 PO 决定并记录理由；
- 禁止：直接 push main、跨团队复制代码、提交 AI 生成且无法讲解的内容。
- 同步机制：本表与《CampusOverflow 团队分工方案》第 1 章保持一致，任一改动需同时更新两处。
