# CampusOverflow 团队实验报告

## Lab 1　团队组建与协作工具链（Sprint 0）

> 湖北文理学院　计算机工程学院　计算机科学与技术专业
> 《软件工程》（CS-SE-301）贯穿项目：CampusOverflow 校园问答平台
> 指导教师：胡春阳　文档版本：v1.0　最后更新：2026-10-10

### 封面信息

| 项目 | 内容 |
| --- | --- |
| 课程名称 | 软件工程 |
| 实验名称 | Lab 1　团队组建与协作工具链 |
| 实验类型 | 综合性、设计性 |
| 建议学时 | 2 学时（第 2 周 / W2） |
| 实验项目 | CampusOverflow 校园问答平台（贯穿项目，Sprint 0） |
| 团队编号 | 第 < > 组（待确认） |
| 团队成员 | 唐志烽（PO）、左焱之（SM）、江思达、何心怡、柯博 |
| 学　　号 | 各成员学号见提交版 Word 文档（公开仓库不录入学号） |
| 班　　级 | 计科2412（以学籍为准） |
| 实验日期 | 2026 年 < > 月 < > 日（W2 实验课） |
| 指导教师 | 胡春阳 |

### 目录

- 一、实验目的
- 二、实验环境
- 三、实验原理
- 四、实验内容与步骤
- 五、实验过程与结果
- 六、实验记录
- 七、实验中的问题与解决
- 八、实验总结与评分自查
- 附录 A　提交物证据索引
- 附录 B　待补证据与逐人操作步骤
- 参考资料

---

## 一、实验目的

本实验不产出业务代码，产出的是"团队能不能持续交付"这件事本身：仓库、权限、看板、沟通渠道与治理章程。用一句话概括——**先把协作的路修好，再让代码上路**。完成本实验后，团队应达到以下目标。

**1. 工具与制度目标**

1. 完成 4–6 人 Scrum 团队组建，明确 PO / SM / Developer 三类角色的职责边界，且 PO 与 SM 由两人分任；
2. 建立团队代码仓库、任务看板、即时沟通群与文档库，并完成 main 分支保护配置（禁止直推、合并前必须评审）；
3. 产出《团队章程 v1.0》：包含团队价值观、沟通约定、开发规约与 Definition of Done（DoD）初版；
4. 固化"提交必须落到仓库"的纪律：IM 里的口头结论一律不算数，结论必须写进 `docs/`。

**2. 协作能力目标**

1. 每一名成员独立完成一次完整的 Git 协作闭环：`clone → branch → commit → push → Pull Request → review → merge`；
2. 能用 `git log` / `git diff` / PR 时间线复原"谁在什么时候改了什么、由谁批准"，即协作过程可审计；
3. 团队能在看板上用 To Do / In Progress / In Review / Done 四列真实反映工作状态，并遵守 WIP 限制。

**3. 工程素养目标**

1. 理解"分支保护 + PR 评审"不是老师要求的流程负担，而是把错误拦在 main 之外的最小代价手段；
2. 建立隐私与合规意识：公开仓库不录入学号、手机号等个人敏感信息，学号只出现在提交版报告与教师可见材料中；
3. 认识到角色是**承诺**而非头衔：写进承诺书的事没做到，团队内部要当面复盘。
---

## 二、实验环境

### 2.1 软件与工具清单

| 类别 | 版本 / 说明 |
| --- | --- |
| 操作系统 | Windows 10/11（需 Git for Windows），团队统一使用 Git Bash 或 PowerShell |
| JDK | OpenJDK 21（LTS） |
| 构建工具 | Maven 3.9.x |
| Node.js | Node.js 20 LTS（前端工程与工具链） |
| 容器 | Docker Desktop（W15 部署阶段使用，Lab 1 前完成安装） |
| IDE | IntelliJ IDEA（后端）、VS Code / WebStorm（前端） |
| 版本控制 | Git 2.40+ |
| 协作平台 | GitHub：仓库、Issue、Projects 看板、Pull Request、分支保护 |
| 即时沟通 | 团队 IM 群（微信 / QQ） |
| 文档载体 | Markdown（与代码同仓、可 diff、可评审、可回溯） |

### 2.2 本次实测环境

| 项 | 实测值 |
| --- | --- |
| 团队仓库 | https://github.com/Flagg32/f26-lab01 （Public，Fork 自 CMU-17-214/f26-lab01） |
| 默认分支 | `main` |
| 团队文档目录 | `campusoverflow/`（团队产物与上游 lab 代码隔离） |
| 协作成员 | 5 人：Flagg32（Admin）、luck970 / hehe309 / houhai6 / Ke-1226（Write） |
| 实测终端 | Windows 11 + Git Bash（MINGW64）+ PowerShell |
| 报告生成方式 | Python 3 + python-docx 脚本由 Markdown 生成 Word 提交版（保证正文与仓库一致） |

**说明**：团队仓库由课程组织提供的 `f26-lab01` Fork 而来，上游仓库中保留了教师发布的 lab 起始代码。团队的全部课程文档集中放在 `campusoverflow/` 子目录，**不改动上游 lab 代码**，以便教师区分"团队工程产物"与"个人 lab 作业"。

---

## 三、实验原理

### 3.1 为什么第一个实验是"团队与工具"，而不是"功能"

CampusOverflow 要经历 4 个 Sprint、16 周、5 个人的并行开发。这类项目的失败很少来自"某个功能写不出来"，而来自协作基础设施缺位后被放大的小问题。表 3-1 把本课程中真实会发生的连锁反应写清楚，作为本实验的设计依据。

**表 3-1　协作基础设施缺位的连锁反应**

| 基础设施缺位 | 直接后果 | 在本项目中的具体表现 | 对应拦截手段 |
| --- | --- | --- | --- |
| main 无分支保护 | 半成品直接进主线 | Sprint 1 两人同时改 `Question.java`，主线一度编译不过 | 分支保护 + PR 评审 |
| 无统一看板 | 任务状态只存在个人脑中 | Sprint 末才发现 US-Q-06（提交回答）无人认领 | 四列看板 + WIP 限制 |
| 无提交规约 | 历史不可读 | 无法用 `git log` 定位"声誉规则是哪次提交改的" | Conventional Commits + 一任务一 PR |
| 无 Definition of Done | "写完"的标准不一致 | 测试未写就合入，Sprint 末集中返工 | 《团队章程 v1.0》DoD 章节 |
| 结论只留在 IM | 决策不可追溯 | 同一分歧反复讨论，结论随聊天记录沉底 | 任何结论必须落到 `docs/` |

因此 Lab 1 的目标不是"多写点代码"，而是把上述四类风险在 Sprint 1 开工前一次性堵掉。

### 3.2 Scrum 三角色与职责边界

**表 3-2　本团队角色分配（5 人编制）**

| 代号 | 姓名 | 角色 | 兼任 | 技术主场 |
| --- | --- | --- | --- | --- |
| A | 唐志烽 | Product Owner（PO） | 前端 Dev（Identity / Discovery 前端） | 需求分析、React + TypeScript |
| B | 左焱之 | Scrum Master（SM） | DevOps / 部署负责人 | CI/CD、Docker、Prometheus |
| C | 江思达 | Developer | 后端领域负责人（Identity + Q&A 子域） | Spring Boot 3、DDD、JPA |
| D | 何心怡 | Developer | 后端实现（Reputation + Discovery 子域） | JPA、Redis、查询优化 |
| E | 柯博 | Developer | 前端 Dev（Q&A / Reputation 前端）+ QA | React + TypeScript、Vitest、Playwright |

**三条硬边界（教学方案 L5 明令，写入承诺书）**

1. PO 与 SM 必须由两人分任，**禁止 PO 兼 SM**——否则"做什么"和"怎么推进"由同一人判断，等于取消了 SM 的纠偏职能；
2. SM 的技术产出限定在 `deploy/` 与 `.github/workflows/`，避免"既当流程主人、又当代码 Owner"；
3. QA 由 E（柯博）兼任，但后端单元 / 集成测试仍由 C（江思达）、D（何心怡）各自负责，E 只做标准制定、抽查与端到端验证。

### 3.3 Git 协作流程（Trunk-Based + PR）

团队采用 Trunk-Based 的简化形态：`main` 永远可编译、可运行；任何改动都必须经由短生命周期分支 + Pull Request 进入 `main`。

**表 3-3　一次协作的七步闭环**

| 步骤 | 动作 | 命令 / 界面 | 纪律要求 |
| --- | --- | --- | --- |
| 1 | 同步主线 | `git switch main` → `git pull --ff-only` | 开工前必须同步，禁止在过期基线上开发 |
| 2 | 切功能分支 | `git switch -c feature/<issue号>-<描述>` | 分支名带 Issue 号，便于回溯 |
| 3 | 本地提交 | `git commit -m "feat(scope): subject"` | 一个任务可含多个提交，但每个提交只做一件事 |
| 4 | 推送远端 | `git push -u origin <分支>` | 推送后立刻开 PR，不"攒着一起推" |
| 5 | 发起 PR | GitHub 界面 | 正文写清"做了什么 / 为什么 / 怎么验证"，并 `Closes #<issue>` |
| 6 | 评审 | 至少 1 名队友 Review + LGTM | 评审人必须看 diff，不能只看标题 |
| 7 | 合并 | Squad（squash）合并到 `main` | 仅 SM / 仓库管理员执行合并；合并后删除远端分支 |

**提交信息规约（Conventional Commits）**：`feat | fix | docs | test | refactor | chore(scope): subject`。例如 `docs(team): 填写团队角色分配（柯博 / 江思达 / 何心怡）`。

### 3.4 看板与 WIP 限制

看板固定四列，列名与 Backlog 的"状态"字段**必须逐字一致**，否则看板与 Backlog 会各说各话。

**表 3-4　四列定义与 WIP 限制**

| 列 | 进入条件 | 离开条件 | WIP 上限 |
| --- | --- | --- | --- |
| To Do | 已被 Sprint 承诺，尚未有人开始 | 有人建立分支并开始编码 | 不限 |
| In Progress | 已建分支，正在编码或写测试 | 代码完成并自测通过，已推送分支 | ≤ 3 |
| In Review | 已开 PR，等待评审 | 至少 1 人 LGTM 且 CI 全绿 | ≤ 3 |
| Done | 已合并 `main` 并通过 DoD 检查 | —— | 不限 |

WIP 限制的意义是**限制并行、暴露阻塞**：当 In Progress 已经 3 张卡片时，第 4 个任务必须等人腾出手，而不是再开一个新分支。

### 3.5 本实验的制度依据

- 角色边界、禁止 PO 兼 SM：教学方案 L5《项目管理与团队组织》；
- 仓库、看板、分支保护、章程四项交付物：教学方案 Lab 1《团队组建与协作工具链》【提交物】；
- DoD 与"结论必须落文档"：教学方案 Sprint 0 团队任务；
- 公开仓库不录入学号等敏感信息：课程思政"数据合规"要求 + 团队自行加严的隐私约定。

---

## 四、实验内容与步骤

### Step 1　团队组建与角色确认（30 分钟）

1. 依据《个人技能矩阵》按**互补原则**自由组队，形成 5 人团队（本课程要求 4–6 人）；
2. 产生 PO 与 SM 各 1 名，其余成员为 Developer；PO 与 SM 不得由同一人担任；
3. 每名成员填写《角色承诺书》，写明"承担什么 + 承诺做到什么 + 什么情况下算没做到"；
4. 承诺书统一落盘到 `docs/team/role-commitment.md`，签字以 commit message 形式完成。

### Step 2　协作工具链配置（40 分钟）

1. Fork 课程组织提供的仓库到团队账号，形成团队工作仓库；
2. 配置分支保护规则：`main` 不允许直推、合并前必须经评审；
3. 创建任务看板：To Do / In Progress / In Review / Done 四列，并录入初始 Backlog（≥ 5 条）；
4. 建立团队 IM 群（微信 / QQ）用于异步沟通，并约定响应时限；
5. 建立文档库：仓库内 `docs/` 目录，按 `team / backlog / domain / api / adr / review / ops / personal` 分类。

关键命令（仓库管理员执行）：

```bash
# 1. 建立本地工作区并与远端建立关系
git init
git add .
git commit -m "chore: initialize CampusOverflow workspace baseline"
git branch -M main
git remote add origin https://github.com/<团队仓库>.git
git push -u origin main

# 2. 核对远端关系（每名成员克隆后执行，确认自己确实连到了团队仓库）
git remote -v
git branch -a
git status
git log --oneline --decorate --graph --all -10
```

### Step 3　Git 协作首次演练（30 分钟）

1. 每名成员 `clone` 团队仓库，从 `main` 切出 `feature/<姓名拼音>-hello` 分支；
2. 在 `docs/team/hello-<name>.md` 中写一段自我介绍并提交；
3. 发起 Pull Request，由**另一名队友** review + LGTM 后由 SM 合并；
4. 验收标准：`main` 分支包含**全体成员**的 hello 文档。

```bash
git switch main
git pull --ff-only origin main
git switch -c feature/<pinyin>-hello
# 编写 docs/team/hello-<name>.md
git add docs/team/hello-<name>.md
git commit -m "docs(team): add self introduction for <name>"
git push -u origin feature/<pinyin>-hello
# 然后在 GitHub 上发起 PR，请求一名队友评审
```

### Step 4　团队章程撰写（20 分钟）

1. 基于课程提供的章程模板完成团队版本；
2. 关键章节：团队价值观、沟通约定、开发规约（含提交与评审规约）、DoD 初版；
3. 提交至 `docs/team/charter-v1.0.md`，由全员签字（commit message 包含签名）。

---

## 五、实验过程与结果

### 5.1 团队组建与角色确认

**5.1.1 组队依据**：按"后端重、前端重、流程重"三类工作量互补组队——2 名后端（C、D）承担四个子域的实现，2 名前端（A、E）承担界面与端到端测试，1 名专职流程与部署（B）。PO 由需求分析能力最强者担任，SM 由最熟悉工具链者担任。

**5.1.2 角色分配结果**

**表 5-1　成员、角色与仓库账号**

| 代号 | 姓名 | 角色 | 兼任 | GitHub 账号 | 仓库权限 |
| --- | --- | --- | --- | --- | --- |
| A | 唐志烽 | Product Owner | 前端 Dev | `luck970` | Write |
| B | 左焱之 | Scrum Master | DevOps / 部署 | `Ke-1226` / `houhai6` / `Flagg32` 之一（待确认） | Write |
| C | 江思达 | Developer（后端领域负责人） | —— | 同上（待确认） | Write |
| D | 何心怡 | Developer（后端实现） | —— | `hehe309` | Write |
| E | 柯博 | Developer（前端 + QA） | QA | 同上（待确认） | Write |

> ⚠️ **待确认项**：仓库 5 个协作账号为 `Flagg32`（管理员）、`luck970`、`hehe309`、`houhai6`、`Ke-1226`。其中 `luck970` = 唐志烽、`hehe309` = 何心怡已由提交作者名核实；`Flagg32`、`houhai6`、`Ke-1226` 与左焱之 / 江思达 / 柯博的一一对应关系需在 Lab 1 现场确认后回填本表，并统一各人的 `git config user.name` 与 `user.email`（建议使用 GitHub noreply 邮箱），便于教师核对提交归属。

**5.1.3 角色承诺书**：已落盘 `docs/team/role-commitment.md`，其中包含 5 人的角色承诺、三条硬边界（禁止 PO 兼 SM / SM 技术产出限定 `deploy/` 与 `.github/workflows/` / QA 只做标准制定与抽查）以及团队共同约定（响应时限、会议纪律、冲突处理、同步机制）。

### 5.2 协作工具链配置

**5.2.1 团队代码仓库**

**表 5-2　仓库实测信息（通过 GitHub REST API 与 `git remote -v` 复核）**

| 项 | 实测值 |
| --- | --- |
| 仓库地址 | https://github.com/Flagg32/f26-lab01 |
| 可见性 | Public |
| 来源 | Fork 自 `CMU-17-214/f26-lab01`（教师提供的起始仓库） |
| 默认分支 | `main` |
| 团队文档目录 | `campusoverflow/`（22 个文档 + `docs/` 子结构） |
| 协作者 | 5 人全部接受邀请（1 名 Admin + 4 名 Write） |

**5.2.2 分支保护**

按 Step 2 要求，`main` 需要"禁止直推 + 合并前必须评审"。配置路径：`Settings → Rules → Rulesets → New branch ruleset`（或 `Settings → Branches → Add branch protection rule`），勾选项如下：

| 规则项 | 取值 | 作用 |
| --- | --- | --- |
| Target branches | `main` | 规则只作用于主线 |
| Restrict deletions | 开启 | 防止误删主线 |
| Block force pushes | 开启 | 防止历史被覆盖 |
| Require a pull request before merging | 开启 | 禁止直推 |
| Required approvals | 1 | 至少 1 人 LGTM |
| Dismiss stale approvals | 开启 | 推送新提交后需重新评审 |
| Require conversation resolution | 开启 | 评审意见必须闭环 |

> ⚠️ **现状**：截至报告生成时，仓库**尚未配置**分支保护（`rulesets` 接口返回空、`protection` 接口 404）。这是 Lab 1 提交物第 1 项的前置条件，须由仓库管理员 `Flagg32` 在验收前完成，并截图留证（见附录 B）。

**5.2.3 任务看板**

看板采用 GitHub Projects（或等价的项目看板工具），四列固定为 To Do / In Progress / In Review / Done，与 `docs/backlog/product-backlog.md` 的状态字段逐字一致。Team 已备好 24 条初始 Backlog（P0 = 8 / P1 = 11 / P2 = 5），满足"初始 Backlog ≥ 5 条"的录入要求。

> ⚠️ **现状**：看板尚未创建（仓库 Projects 接口返回 404），需由 SM 按附录 B 的步骤建立并截图。看板截图需体现"至少 5 条 To Do 卡片带有负责人"。

**5.2.4 沟通渠道与约定**

| 项 | 约定 |
| --- | --- |
| 渠道 | 团队 IM 群（日常）+ 仓库 Issue（任务）+ PR 评论（技术讨论） |
| 响应时限 | 工作日 4 小时内回复；标记为阻塞的消息 1 小时内响应 |
| 结论落盘 | 任何影响范围、接口或规则的结论，必须写进 `docs/` 并提交，IM 里的结论不算数 |
| 每日节奏 | 每日 1 次 Daily Standup（文字纪要即可，SM 汇总） |

### 5.3 Git 协作首次演练（实测证据）

**5.3.1 已完成的 Pull Request（实测）**

**表 5-3　PR 实测清单（经 GitHub REST API 核对）**

| PR | 分支 | 作者 | 变更文件数 | 合并时间（北京时间） | 评审 | 状态 |
| --- | --- | --- | --- | --- | --- | --- |
| #1 | `docs/campusoverflow-po` | luck970 | 22 | 2026-10-10 19:12 | 无 | 已合并 |
| #2 | `feature/docs-luck970-team-roster` | luck970 | 1 | 2026-10-10 19:16 | 无 | 已合并 |
| #3 | `feature/docs-luck970-roster-5p` | luck970 | 1 | 2026-10-10 19:18 | 无 | 已合并 |
| #4 | `feature/docs-luck970-po-name` | luck970 | 1 | 2026-10-10 19:20 | 无 | 已合并 |
| #5 | `feature/docs-luck970-interviewees` | luck970 | 1 | 2026-10-10 19:22 | 无 | 已合并 |

**5.3.2 主线上与团队文档直接相关的提交**

**表 5-4　提交记录（北京时间）**

| 提交 | 作者 | 说明 |
| --- | --- | --- |
| `ac06698` | luck970 | `docs(campusoverflow): 加入项目文档骨架（愿景、Backlog、Gherkin、章程、PO 手册）` (#1) |
| `09e765f` | luck970 | `docs(team): 填写团队角色分配（柯博 / 江思达 / 何心怡）` (#2) |
| `837765b` | luck970 | `docs(team): 更新为 5 人团队角色分配（新增左焱之任 SM）` (#3) |
| `47f679b` | luck970 | `docs(team): 补全 PO 姓名（唐志烽）与生效日期` (#4) |
| `6b81a9e` | luck970 | `docs(backlog): 确定 3 位受访者（柯博 / 江思达 / 何心怡）并标注待补视角` (#5) |
| `3bc8443` | Flagg32 | `Fix README declaration: merge duplicated section, unescape markdown` |
| `acd5c4a` | hehe309 | `Add files via upload` |
| `c9ef15d` | houhai6 | `docs: add Milestone 3 verification record for isAvailable` |
| `8dbabf3` | Hammad Ahmad（上游） | `Lab 1 starter: course infrastructure and your first supervised task`（教师基线） |

> 说明：`8dbabf3` 及其后的 lab 起始代码提交属于教师提供的实验基础设施与个人 lab 任务，不计入团队协作指标；本表只统计团队文档协作部分。

**5.3.3 七步闭环逐项自查**

**表 5-5　协作流程达标情况（依据 3.3 节七步闭环）**

| 步骤 | 是否达标 | 证据 / 缺口 |
| --- | --- | --- |
| 1 同步主线（`git pull --ff-only`） | ✅ 达标 | 每次开工前同步，无过期基线合并 |
| 2 分支命名规范 | ✅ 达标 | `feature/docs-luck970-*`、`docs/campusoverflow-po` |
| 3 提交信息规范 | ✅ 达标 | 全部符合 Conventional Commits |
| 4 推送后立即开 PR | ✅ 达标 | 5 个 PR 与分支一一对应，无"攒着一起推" |
| 5 PR 正文关联 Issue（`Closes #n`） | ❌ 未达标 | 5 个 PR 均未关联 Issue，Sprint 0 亦未建立 Issue |
| 6 至少 1 名队友评审 + LGTM | ❌ 未达标 | 5 个 PR 的评审记录均为空，经 API 逐条核实 |
| 7 Squash 合并 + 删除远端分支 | ⚠️ 部分达标 | 已合并入 `main`；但 5 个远端分支仍残留未删除 |

**缺口整改计划**（Lab 1 验收前完成，具体命令见附录 B）：

1. 由 SM 建立 Sprint 0 的 3 个 Issue（团队章程、看板初始化、成员 hello 文档），后续 PR 必须 `Closes #n`；
2. 每人至少**评审 1 个非本人 PR**并给出 LGTM 或 Request changes，评审意见必须针对 diff 内容；
3. 合并后由合并人删除远端分支，仓库只保留 `main` 与在建分支。

### 5.4 团队章程 v1.0

章程需包含四个关键章节，其内容已在团队文档中成形，待正式落盘为 `docs/team/charter-v1.0.md`（由全员签字后提交）。

**表 5-6　《团队章程 v1.0》章节与内容来源**

| 章节 | 必须写清的内容 | 现有依据 |
| --- | --- | --- |
| 一、团队价值观 | 质量优先于进度、结论必须落文档、不提交自己讲不清的代码 | 《团队分工方案》1.3 角色边界 |
| 二、沟通约定 | 渠道（IM / Issue / PR 评论）、响应时限（工作日 4 小时、阻塞 1 小时）、会议纪律 | 《角色承诺书》三、团队共同约定 |
| 三、开发规约 | Git 流程（Trunk-Based + PR）、Conventional Commits、分支命名、评审规则 | 《团队分工方案》2.1、2.2 |
| 四、DoD 初版 | 代码合并 main / 单测覆盖 ≥ 70%（核心领域 ≥ 90%）/ CI 全绿 / 1 人 LGTM / Gherkin 通过 / Smoke Test 通过 / OpenAPI 同步 / ADR 已写 / README 更新 | 《团队分工方案》2.4、《Sprint 1 Planning 纪要》六 |

### 5.5 提交物对照表

**表 5-7　Lab 1 四项提交物现状**

| 序号 | 提交物 | 交付形式 | 现状 |
| --- | --- | --- | --- |
| 1 | 团队代码仓库 URL（分支保护已配置） | 仓库链接 + 分支保护截图 | ⚠️ 仓库已建、5 人已入伙；**分支保护未配置** |
| 2 | 团队看板截图（初始 Backlog ≥ 5 条） | Projects 看板截图 | ⚠️ **看板未建立**；Backlog 已备 24 条可直接录入 |
| 3 | 《团队章程 v1.0》Markdown | `docs/team/charter-v1.0.md` | ⚠️ 《角色承诺书》已提交；**章程待落盘** |
| 4 | 每位成员的首次 PR 合并记录截图 | 5 张 PR 页面截图 | ⚠️ 仅 1/5 成员（唐志烽）有合并 PR，**其余 4 人的 hello PR 待补** |

> 本表的 ⚠️ 项在报告提交前应逐项关闭；关闭方法逐条写在**附录 B**。

---

## 六、实验记录

### 表 6-1　Lab 1 现场记录（学生填写）

| # | 时间 | 地点 / 方式 | 主持 | 参与人 | 主要决议 | 后续动作（责任人） |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | 2026-< >-< > < >:< >–< >:< > | 实验室 / 线下 | 左焱之（SM） | 全员 5 人 | 确认 5 人角色分配与三条硬边界 | 更新角色承诺书（A） |
| 2 | 2026-< >-< > < >:< >–< >:< > | 线上串会 | 左焱之（SM） | 全员 5 人 | 首次 Daily Standup，确认当日阻塞 | 汇总纪要（B） |
| 3 | 2026-< >-< > < >:< >–< >:< > | 实验室 / 线下 | 唐志烽（PO） | 全员 5 人 | 章程 v1.0 定稿、全员签字 | 提交 `docs/team/charter-v1.0.md`（B） |

### 表 6-2　协作指标实测（截至 2026-10-10，经 GitHub API 核实）

| 指标 | 实测值 | 目标 | 达标 |
| --- | --- | --- | --- |
| 仓库协作者数 | 5 | 4–6 | ✅ |
| 有提交记录的成员 | 4 / 5 | 5 / 5 | ❌ |
| 有合并 PR 的成员 | 1 / 5 | 5 / 5 | ❌ |
| PR 总数 / 已合并 | 5 / 5 | ≥ 5 | ✅ |
| 带评审记录的 PR | 0 / 5 | 100% | ❌ |
| PR 关联 Issue 的比例 | 0 / 5 | 100% | ❌ |
| 提交信息符合规约的比例 | 100% | 100% | ✅ |
| 文档产出 | 22 个文件 | ≥ 5 | ✅ |
| main 分支保护 | 未配置 | 已配置 | ❌ |
| 四列看板 | 未建立 | 可用且含 ≥ 5 条卡片 | ❌ |

> 表 6-2 中 ❌ 项均为 Lab 1 验收前必须关闭的缺口，整改步骤见附录 B。

### 表 6-3　工具链就绪自查表（每名成员逐项打勾）

| 检查项 | A 唐志烽 | B 左焱之 | C 江思达 | D 何心怡 | E 柯博 |
| --- | --- | --- | --- | --- | --- |
| Git 2.40+ 已安装并可执行 `git --version` | | | | | |
| JDK 21 与 Maven 3.9 可用（`java -version` / `mvn -v`） | | | | | |
| Node.js 20 可用（`node -v`） | | | | | |
| Docker Desktop 已安装并可启动 | | | | | |
| 已 clone 团队仓库且 `git remote -v` 指向团队仓库 | | | | | |
| `git config user.name / user.email` 已统一为真实姓名 + noreply 邮箱 | | | | | |
| 已接受仓库协作者邀请（能 push 自己的分支） | | | | | |
| 能独立完成一次 `branch → commit → push → PR` | | | | | |
| 已完成至少 1 次他人 PR 的评审（评论或 LGTM） | | | | | |

---

## 七、实验中的问题与解决

**问题 1　远端仓库地址与本地目录不对应，首次推送失败**

- 现象：执行 `git remote add origin https://github.com/<luck970>/campusoverflow.git` 后，终端报 `bash: luck970: No such file or directory`。
- 原因：命令里的 `< >` 被 shell 解释为输入重定向，占位符没有被替换成真实值；同时团队尚未确定统一仓库。
- 解决：去掉尖括号、写全真实 URL；团队统一以 `Flagg32/f26-lab01` 为唯一工作仓库，并把它作为唯一 `origin`。
- 教训：**命令里的占位符必须替换后再执行**，文档模板中的 `<>` 不能直接粘贴到终端。

**问题 2　Fork 工作流的合并目标容易误指向上游**

- 现象：在 Fork 仓库上发起 PR 时，GitHub 默认的 base 仓库可能选到上游课程仓库（`CMU-17-214/f26-lab01`），而不是团队自己的 Fork。
- 解决：约定"发 PR 前先看 base 仓库"：**base repository 必须是 `Flagg32/f26-lab01`，base 分支 `main`；head repository 是各自 Fork / 同一仓库的功能分支**。误开的 PR 立即关闭并说明原因。
- 教训：Fork 流程下 `compare` 页面有 base / head 两组仓库，是本课程最容易踩的坑，已写进团队章程"开发规约"章节。

**问题 3　公开仓库中的隐私与合规**

- 现象：提交物索引表需要学号，但仓库是 Public，直接写入学号属于不必要的信息暴露。
- 解决：仓库内文档**只记录真实姓名与 GitHub 账号**，学号仅出现在提交版 Word 报告与本表第 2 节这类教师可见材料中；公开仓库不再出现学号、手机号等个人信息。
- 依据：课程思政"数据合规"要求（《个人信息保护法》《数据安全法》）。

**问题 4　提交作者身份写法不统一**

- 现象：历史提交中的作者名出现 `ke-1226`、`2024117204何心怡`、`houhai6` 等多种写法，教师核对提交归属时需要来回比对。
- 解决：全员统一 `git config --global user.name "<真实姓名>"` 与 noreply 邮箱，并在章程里写明"提交作者名 = 真实姓名"。

**问题 5　已合并分支残留、PR 无评审**

- 现象：5 个 PR 合并后远端分支未删除；5 个 PR 均无评审记录（分支保护尚未开启，流程靠自觉）。
- 解决：合并后由合并人删除远端分支；先开启分支保护，再用"每人不低于 1 次对他人 PR 的评审"补齐记录。
- 教训：**流程约束必须由平台强制执行**，只写进文档的约定在赶进度时一定会被绕过。

---

## 八、实验总结与评分自查

### 8.1 本实验实际完成的工作

1. 组建 5 人 Scrum 团队，产出《角色承诺书》（`docs/team/role-commitment.md`），明确 PO / SM / Dev 三类角色与三条硬边界；
2. 建立团队仓库并完成 5 人权限配置，团队文档在 `campusoverflow/docs/` 下形成 8 个分类目录、22 个文档；
3. 打通 Git 协作闭环：5 个 Pull Request 全部经分支合并入 `main`，提交信息 100% 符合 Conventional Commits；
4. 产出 Sprint 0 的产品侧基础文档：产品愿景、24 条 Product Backlog、3 条 Gherkin 验收场景、声誉规则、Sprint 1 Planning 草案。
5. 形成可核查的证据链：所有结论都能用 `git log` / PR 时间线 / GitHub API 复核，不依赖口头说明。

### 8.2 尚未达标的事项（须在 Lab 1 验收前关闭）

1. `main` 分支保护未配置；
2. 四列看板未建立；
3. 《团队章程 v1.0》未正式落盘；
4. 4 名成员尚无合并 PR，5 个 PR 均无评审记录，PR 未关联 Issue。

### 8.3 评分要点自查

**表 8-1　对照 Lab 1 评分要点（教学方案）**

| 评分要点 | 权重 | 自评 | 依据 / 差距 |
| --- | --- | --- | --- |
| 工具链配置完整性（仓库、看板、IM、分支保护） | 30% | 部分达标 | 仓库与 IM 已就绪；看板与分支保护待补 |
| 团队章程质量（具体可执行、覆盖关键场景） | 30% | 部分达标 | 承诺书与规约已成文，待合并为章程 v1.0 并全员签字 |
| Git 协作熟练度（分支、PR、评审、合并规范） | 30% | 部分达标 | 分支与提交规范达标；评审环节缺失、需补齐 5 人各自的 PR |
| 团队氛围与协作情况 | 10% | 达标 | 角色边界清晰，全员入伙并完成文档协作 |

### 8.4 下一步（Lab 2 前置作业）

1. 完成 **3 份真实用户访谈**（受访者已定：柯博、江思达、何心怡），并补足"助教 / 教师"两个视角（教学方案 L3 要求）；
2. 在 Lab 2 现场产出用户故事地图与 Sprint 1 Backlog 终版；
3. 关闭 8.2 中全部未达标事项。

---

## 附录 A　提交物证据索引

**表 A-1　证据索引（链接均指向团队仓库）**

| 类型 | 名称 | 链接 | 负责人 |
| --- | --- | --- | --- |
| 仓库 | 团队工作仓库 | https://github.com/Flagg32/f26-lab01 | Flagg32 |
| 成员 | 协作者列表（5 人） | https://github.com/Flagg32/f26-lab01/settings/access | Flagg32 |
| PR | #1 项目文档骨架（22 个文件） | https://github.com/Flagg32/f26-lab01/pull/1 | luck970 |
| PR | #2 团队角色分配 | https://github.com/Flagg32/f26-lab01/pull/2 | luck970 |
| PR | #3 5 人角色分配（新增 SM） | https://github.com/Flagg32/f26-lab01/pull/3 | luck970 |
| PR | #4 补全 PO 姓名与生效日期 | https://github.com/Flagg32/f26-lab01/pull/4 | luck970 |
| PR | #5 确定 3 位受访者 | https://github.com/Flagg32/f26-lab01/pull/5 | luck970 |
| 提交 | `ac06698` 文档骨架 | https://github.com/Flagg32/f26-lab01/commit/ac06698 | luck970 |
| 提交 | `6b81a9e` 受访者定稿 | https://github.com/Flagg32/f26-lab01/commit/6b81a9e | luck970 |
| 文档 | 团队角色承诺书 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/team/role-commitment.md | 全员 |
| 文档 | 产品愿景 v1.0 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/backlog/vision.md | 唐志烽 |
| 文档 | Product Backlog（24 条） | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/backlog/product-backlog.md | 唐志烽 |
| 文档 | Gherkin 验收场景（3 条） | https://github.com/Flagg32/f26-lab01/tree/main/campusoverflow/docs/backlog/stories | 唐志烽 |
| 文档 | Sprint 1 Planning 纪要 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/backlog/sprint-1-plan.md | 唐志烽 |
| 文档 | 声誉 / 徽章 / 悬赏规则 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/backlog/rules-reputation.md | 唐志烽 |
| 文档 | 用户访谈记录（3 人） | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/backlog/interviews.md | 唐志烽 |
| 文档 | 团队分工与实施手册 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/CampusOverflow团队分工方案.md | 唐志烽 |
| 文档 | PO 执行手册 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/CampusOverflow-PO执行手册.md | 唐志烽 |
| 文档 | 本实验报告 | https://github.com/Flagg32/f26-lab01/blob/main/campusoverflow/docs/reports/lab1-team-report.md | 唐志烽 |

**表 A-2　需要人工截图的证据（API 无法替代）**

| # | 截图内容 | 要求 | 状态 |
| --- | --- | --- | --- |
| 1 | 分支保护配置页 | 能看清规则名、目标分支 `main`、Require PR 与 Required approvals = 1 | ⬜ 待补 |
| 2 | 看板四列 | 能看清 To Do / In Progress / In Review / Done 与 ≥ 5 条带负责人的卡片 | ⬜ 待补 |
| 3–7 | 5 名成员各自的首次 PR 合并页 | 每张能看清作者、评审人（LGTM）、合并时间 | ⬜ 待补 |
| 8 | 首次 Daily Standup 纪要 | 文字纪要即可（含日期、参与人、阻塞项） | ⬜ 待补 |

---

## 附录 B　待补证据与逐人操作步骤

> 本附录是 Lab 1 验收前的"施工图纸"，按角色分工执行即可。所有命令中的 `<...>` 必须替换为真实值后再执行。

### B.1　仓库管理员（Flagg32）· 3 项

**B.1.1 配置 `main` 分支保护（5 分钟）**

1. 打开 https://github.com/Flagg32/f26-lab01/settings/rules → **New ruleset → New branch ruleset**；
2. Ruleset Name 填 `protect-main`，Enforcement status 选 **Active**；
3. Target branches → **Add target → Include default branch**（或直接填 `main`）；
4. 勾选：`Restrict deletions`、`Block force pushes`、`Require a pull request before merging`；
5. 在 Require a pull request 下设置 **Required approvals = 1**、勾选 `Dismiss stale pull request approvals when new commits are pushed`；
6. 勾选 `Require conversation resolution before merging`；
7. Create → 回到仓库 `Branches` 页面截图留证。
   （若团队用旧版规则：`Settings → Branches → Add branch protection rule`，Branch name pattern 填 `main`，勾选同样几项。）

**B.1.2 建立四列看板（5 分钟）**

1. 仓库顶部 **Projects → New project → Board**，命名 `CampusOverflow Sprint 0`；
2. 建四列并改名：`To Do`、`In Progress`、`In Review`、`Done`；
3. 从 `docs/backlog/product-backlog.md` 挑 8 条 P0 故事建立卡片（Issues 或 Draft item 均可），**每条卡片写上负责人**（C / D / A+E）；
4. 截整屏图（需看到四列列名与至少 5 条带负责人的卡片）。

**B.1.3 建立 Sprint 0 的 3 个 Issue（5 分钟）**

每个 Issue 用"背景—任务—验收标准—不包含内容"四段式写，并设置 Assignee：

| Issue | 标题 | 负责人 | 验收标准（摘要） |
| --- | --- | --- | --- |
| #6 | [docs] 产出《团队章程 v1.0》并全员签字 | 左焱之 | `docs/team/charter-v1.0.md` 合入 main，commit message 含 5 人签名 |
| #7 | [chore] 初始化四列看板与分支保护 | 左焱之 | 看板四列可用；`main` 分支保护生效并留截图 |
| #8 | [docs] 每名成员提交 `docs/team/hello-<name>.md` | 全员 | main 上存在 5 份 hello 文档，每份对应 1 个已评审 PR |

### B.2　全体成员（A–E）· hello PR 演练（每人 10 分钟）

```bash
# 1. 统一提交身份（各自执行一次，姓名换成自己的）
git config --global user.name "柯博"
git config --global user.email "<GitHub 数字 ID>+<账号>@users.noreply.github.com"

# 2. 同步主线并切分支（<pinyin> 换成自己的姓名拼音）
git switch main
git pull --ff-only origin main
git switch -c feature/<pinyin>-hello

# 3. 新建自我介绍文件并写入内容
#    文件路径：docs/team/hello-<pinyin>.md
git add docs/team/hello-<pinyin>.md
git commit -m "docs(team): add self introduction for <姓名>"
git push -u origin feature/<pinyin>-hello
```

推送后到 GitHub 点击 **Compare & pull request**：

1. **务必确认 base repository 是 `Flagg32/f26-lab01`、base 分支是 `main`**（不要选到上游课程仓库）；
2. 标题：`docs(team): add self introduction for <姓名>`；正文写"新增内容 / 为什么 / 如何验证"，并写 `Closes #8`；
3. 右栏 Reviewers 选 1 名队友，Assignees 选自己；
4. 等队友 LGTM 后由 SM 合并。

**评审他人的 PR（每人不低于 1 次）**：打开队友的 PR → `Files changed` → 逐段点 `+` 写意见 → 右上 `Review changes` → 选 **Approve**（附一句具体意见，例如"自我介绍里补一下你的技术主场"）→ `Submit review`。

### B.3　SM（左焱之）· 流程收口（10 分钟）

**B.3.1 清理已合并的残留远端分支**

```bash
git push origin --delete docs/campusoverflow-po
git push origin --delete feature/docs-luck970-team-roster
git push origin --delete feature/docs-luck970-roster-5p
git push origin --delete feature/docs-luck970-po-name
git push origin --delete feature/docs-luck970-interviewees
```

**B.3.2 主持首次 Daily Standup 并留纪要**：三问（昨天做了什么 / 今天做什么 / 有什么阻塞），把结论写成文字纪要提交到 `docs/team/standup-2026-< >-< >.md`，作为"SM 真的在主持"的证据。

**B.3.3 把章程落到仓库**：新建 `docs/team/charter-v1.0.md`，把表 5-6 的四个章节写全，由 5 人分别在 commit message 中签名（可拆成 5 次 `sign(team): <姓名> 签署团队章程 v1.0`）。

### B.4　全员（15 分钟）· 提交前终检

- [ ] `main` 分支保护已开启且留截图（B.1.1）
- [ ] 四列看板已建立、≥ 5 条卡片带负责人、已截图（B.1.2）
- [ ] 3 个 Issue 已建立，PR 正文均含 `Closes #n`（B.1.3）
- [ ] `docs/team/hello-<name>.md` 5 份齐全并已合入 main（B.2）
- [ ] 每名成员至少 1 次对他人 PR 的评审记录（B.2 末）
- [ ] 残留分支已清理（B.3.1）
- [ ] 首次 Daily Standup 纪要已提交（B.3.2）
- [ ] `docs/team/charter-v1.0.md` 已全员签字并合入 main（B.3.3）
- [ ] 表 6-3 工具链自查表 5 人全部打勾
- [ ] 8 张截图已收齐并随报告提交（表 A-2）

---

## 参考资料

1. 《软件工程》课程教学方案 v1.0（2026 秋季学期，执笔：胡春阳）—— 3.2 教学进度总表、Lab 1《团队组建与协作工具链》；
2. CampusOverflow 团队分工与实施手册（本仓库 `campusoverflow/CampusOverflow团队分工方案.md`）；
3. CampusOverflow · PO 执行手册（本仓库 `campusoverflow/CampusOverflow-PO执行手册.md`）；
4. Scrum Guide 2020 —— Product Owner / Scrum Master / Developers 的责任边界；
5. GitHub Docs —— *About rulesets*、*About protected branches*、*Reviewing proposed changes in a pull request*；
6. Conventional Commits 1.0.0 —— 提交信息规范；
7. Jeff Patton《用户故事地图》（Lab 2 需求建模工作坊预习材料）。
