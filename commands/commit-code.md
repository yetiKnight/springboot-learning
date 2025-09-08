# Git 提交代码命令参考

## 快速执行命令

### 检查状态
```bash
git status
git diff
git diff --cached
```

### 添加文件
```bash
git add .
git add <文件名>
git add <目录名>/
git add -i
```

### 提交代码
```bash
git commit -m "提交信息"
git commit
git commit --amend
```

### 推送代码
```bash
git push
git push origin <分支名>
git push -u origin <分支名>
```

### 快速提交流程
```bash
# 一键提交并推送
git add . && git commit -m "feat: 添加新功能" && git push

# 检查状态后提交
git status && git add . && git commit -m "fix: 修复问题" && git push
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

### 查看历史
```bash
git log --oneline
git log --oneline -5
git show <提交hash>
```

### 撤销操作
```bash
git checkout -- <文件名>
git reset HEAD <文件名>
git reset --soft HEAD~1
git reset --hard HEAD~1
```

### 分支操作
```bash
git branch <分支名>
git checkout -b <分支名>
git checkout <分支名>
git merge <分支名>
git branch -d <分支名>
```

### 常用组合命令
```bash
# 查看状态并提交
git status && git add . && git commit -m "docs: 更新文档" && git push

# 查看修改内容后提交
git diff && git add . && git commit -m "style: 代码格式调整" && git push

# 查看提交历史
git log --oneline -10

# 撤销最后一次提交（保留修改）
git reset --soft HEAD~1

# 创建新分支并切换
git checkout -b feature/new-feature

# 合并分支并删除
git merge feature/new-feature && git branch -d feature/new-feature
```

## 快速命令模板

### 新功能开发
```bash
git checkout -b feature/功能名称
# 开发完成后
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

## 注意事项

1. **提交前检查**：确保代码能正常编译和运行
2. **提交信息**：使用清晰、简洁的提交信息
3. **原子性提交**：每次提交只做一件事
4. **及时提交**：完成一个功能就及时提交
5. **分支管理**：重要功能使用独立分支开发
6. **代码审查**：重要修改建议先创建PR进行代码审查
