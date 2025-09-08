# Git 提交代码执行脚本

## 一键提交命令

### 智能提交（推荐）
```bash
git add . && git status && echo "准备提交所有修改..." && git commit -m "feat: 更新代码" && git push
```

### 检查后提交
```bash
git status && git diff --stat && git add . && git commit -m "feat: 更新代码" && git push
```

### 查看修改后提交
```bash
git diff && git add . && git commit -m "feat: 更新代码" && git push
```

### 强制提交（谨慎使用）
```bash
git add . && git commit -m "feat: 更新代码" && git push --force
```

## 快速执行命令

### 检查状态
```bash
git status
```

### 查看修改内容
```bash
git diff
```

### 查看已暂存内容
```bash
git diff --cached
```

### 添加所有文件
```bash
git add .
```

### 添加指定文件
```bash
git add <文件名>
```

### 添加指定目录
```bash
git add <目录名>/
```

### 交互式添加
```bash
git add -i
```

### 提交代码（带消息）
```bash
git commit -m "提交信息"
```

### 提交代码（打开编辑器）
```bash
git commit
```

### 修改最后一次提交
```bash
git commit --amend
```

### 推送到远程仓库
```bash
git push
```

### 推送到指定分支
```bash
git push origin <分支名>
```

### 设置上游分支并推送
```bash
git push -u origin <分支名>
```

## 一键执行命令

### 快速提交并推送
```bash
git add . && git commit -m "feat: 添加新功能" && git push
```

### 检查状态后提交
```bash
git status && git add . && git commit -m "fix: 修复问题" && git push
```

### 查看修改后提交
```bash
git diff && git add . && git commit -m "style: 代码格式调整" && git push
```

## 提交信息规范

### 格式
```
<类型>(<范围>): <简短描述>

<详细描述>

<相关Issue>
```

### 类型说明
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整（不影响功能）
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

### 示例
```
feat(controller): 添加用户管理API接口

- 新增用户CRUD操作
- 添加用户状态管理
- 实现分页查询功能

Closes #123
```

## 常用操作命令

### 查看提交历史
```bash
git log --oneline
```

### 查看最近5次提交
```bash
git log --oneline -5
```

### 查看最近10次提交
```bash
git log --oneline -10
```

### 查看指定提交详情
```bash
git show <提交hash>
```

### 撤销工作区修改
```bash
git checkout -- <文件名>
```

### 撤销暂存区文件
```bash
git reset HEAD <文件名>
```

### 撤销最后一次提交（保留修改）
```bash
git reset --soft HEAD~1
```

### 撤销最后一次提交（丢弃修改）
```bash
git reset --hard HEAD~1
```

### 创建新分支
```bash
git branch <分支名>
```

### 创建并切换到新分支
```bash
git checkout -b <分支名>
```

### 切换到指定分支
```bash
git checkout <分支名>
```

### 合并分支
```bash
git merge <分支名>
```

### 删除分支
```bash
git branch -d <分支名>
```

## 常用组合命令

### 查看状态并提交
```bash
git status && git add . && git commit -m "docs: 更新文档" && git push
```

### 查看修改内容后提交
```bash
git diff && git add . && git commit -m "style: 代码格式调整" && git push
```

### 创建新分支并切换
```bash
git checkout -b feature/new-feature
```

### 合并分支并删除
```bash
git merge feature/new-feature && git branch -d feature/new-feature
```

## 快速命令模板

### 新功能开发
```bash
git checkout -b feature/功能名称
```

### 开发完成后提交
```bash
git add . && git commit -m "feat: 添加新功能" && git push
```

### 修复问题
```bash
git add . && git commit -m "fix: 修复问题描述" && git push
```

### 文档更新
```bash
git add . && git commit -m "docs: 更新文档" && git push
```

### 代码重构
```bash
git add . && git commit -m "refactor: 重构代码" && git push
```

### 代码格式调整
```bash
git add . && git commit -m "style: 代码格式调整" && git push
```

### 测试相关
```bash
git add . && git commit -m "test: 添加测试用例" && git push
```

### 构建工具更新
```bash
git add . && git commit -m "chore: 更新构建配置" && git push
```

## 使用说明

### 当你说"提交一下代码"时，我会执行：
```bash
git add . && git status && echo "准备提交所有修改..." && git commit -m "feat: 更新代码" && git push
```

### 如果需要查看修改内容，我会执行：
```bash
git diff && git add . && git commit -m "feat: 更新代码" && git push
```

### 如果需要检查状态，我会执行：
```bash
git status && git diff --stat && git add . && git commit -m "feat: 更新代码" && git push
```

### 其他常用场景命令
```bash
# 查看当前状态
git status

# 查看修改内容
git diff

# 只添加特定文件
git add src/main/java/com/learning/controller/UserController.java && git commit -m "feat: 添加用户管理功能" && git push

# 查看提交历史
git log --oneline -5
```

## 注意事项

1. **提交前检查**：确保代码能正常编译和运行
2. **提交信息**：使用清晰、简洁的提交信息
3. **原子性提交**：每次提交只做一件事
4. **及时提交**：完成一个功能就及时提交
5. **分支管理**：重要功能使用独立分支开发
6. **代码审查**：重要修改建议先创建PR进行代码审查
