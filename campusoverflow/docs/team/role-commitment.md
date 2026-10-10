# 团队角色承诺书

> 提交人：各成员（签字以 commit message 形式完成）    生效日期：<YYYY-MM-DD>

## 一、角色分配

| 代号 | 姓名 | 角色 | 兼任 | 技术主场 |
| --- | --- | --- | --- | --- |
| A | <姓名> | Product Owner（PO） | 前端 Dev（Identity / Discovery） | 需求分析、React + TS |
| B | <姓名> | Scrum Master（SM） | DevOps / 部署 | CI/CD、Docker、监控 |
| C | <姓名> | Developer | 后端领域负责人 | Spring Boot、DDD |
| D | <姓名> | Developer | 后端实现 | JPA、Redis、查询优化 |
| E | <姓名> | Developer | 前端 + QA | React + TS、Vitest、Playwright |

> 团队规模若为 4 人：由 D 兼 QA，Discovery 前端降为 P2；若为 6 人：E 专职前端，新增 F 专职 QA。

## 二、角色承诺

### PO 承诺
1. 维护 Product Backlog，保证每条故事有优先级与验收标准；
2. 主持每次 Sprint Planning，明确 Sprint Goal；
3. 主持每次 Sprint Review，用真实系统演示验收；
4. 拒绝无依据的范围蔓延，并把拒绝理由写进 Backlog 变更记录。

签名：_____________（commit message：`sign(team): <姓名> 承诺担任 PO`）

### SM 承诺
1. 主持 Daily Standup 与 Retrospective，当场产出纪要；
2. 每日更新看板，维护 WIP 限制；
3. 清除障碍，阻塞超过 24 小时升级到教师；
4. 维护 main 分支保护与合并纪律。

签名：_____________（commit message：`sign(team): <姓名> 承诺担任 SM`）

### Dev 承诺
1. 只认领自己能在本 Sprint 完成的故事；
2. 每完成一个任务即提 PR，走评审后合并；
3. 对自己代码的质量与上线后表现负责；
4. 每周至少评审 2 个他人 PR。

签名：_____________（commit message：`sign(team): <姓名> 承诺担任 Dev`）

## 三、团队共同约定

- 响应时限：工作日消息 4 小时内回复，紧急阻塞 1 小时内；
- 会议纪律：迟到者负责下次会议纪要；无故缺席记入贡献度互评依据；
- 沟通渠道：日常用 IM 群，决策必须落到仓库文档（IM 里的结论不算数）；
- 冲突处理：技术分歧以 ADR 记录并表决，产品分歧以 PO 决定并记录理由；
- 禁止：直接 push main、跨团队复制代码、提交 AI 生成且无法讲解的内容。
