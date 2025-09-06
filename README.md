# SpringBoot 系统性学习项目

## 📚 项目简介

这是一个专门为高级开发工程师面试准备的SpringBoot学习项目，通过完整的项目实践来系统性地复习SpringBoot核心知识点。

## 🎯 学习目标

- 系统性地复习SpringBoot核心概念和原理
- 通过项目实践掌握SpringBoot的高级特性
- 为高级开发工程师面试做好充分准备
- 建立完整的知识点笔记和面试题库

## 🏗️ 项目结构

```
springboot-learning/
├── src/main/java/com/learning/
│   ├── SpringbootLearningApplication.java     # 主启动类
│   ├── annotation/                            # 自定义注解
│   │   └── LogExecutionTime.java
│   ├── aspect/                               # AOP切面
│   │   ├── LoggingAspect.java
│   │   └── PerformanceAspect.java
│   ├── config/                               # 配置类
│   │   ├── AppConfig.java
│   │   └── CustomAutoConfiguration.java
│   ├── controller/                           # 控制器层
│   │   ├── UserController.java
│   │   ├── OrderController.java
│   │   ├── AsyncController.java
│   │   ├── ScheduledController.java
│   │   ├── CacheController.java
│   │   ├── PerformanceController.java
│   │   └── HealthController.java
│   ├── entity/                              # 实体类
│   │   ├── User.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── exception/                           # 异常处理
│   │   ├── GlobalExceptionHandler.java
│   │   ├── BusinessException.java
│   │   └── ResourceNotFoundException.java
│   ├── repository/                          # 数据访问层
│   │   ├── UserRepository.java
│   │   └── OrderRepository.java
│   └── service/                             # 服务层
│       ├── UserService.java
│       ├── OrderService.java
│       ├── AsyncService.java
│       ├── ScheduledService.java
│       └── CacheService.java
├── src/main/resources/
│   └── application.yml                      # 配置文件
├── src/test/java/com/learning/
│   └── SpringbootLearningApplicationTests.java
├── src/test/resources/
│   └── application-test.yml                 # 测试配置
├── pom.xml                                 # Maven配置
├── NOTES.md                                # 学习笔记
├── INTERVIEW_NOTES.md                      # 面试知识点总结
├── INTERVIEW_QUESTIONS.md                  # 面试题库
└── README.md                               # 项目说明
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- SpringBoot 3.2.0

### 运行项目

```bash
# 克隆项目
git clone <repository-url>
cd springboot-learning

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 运行测试
mvn test
```

### 访问应用

- 应用地址：http://localhost:8080/api
- H2控制台：http://localhost:8080/api/h2-console
- Actuator监控：http://localhost:8080/api/actuator

## 📖 学习内容

### 第一阶段：基础框架搭建
- [x] 项目初始化和基础配置
- [x] 核心注解和自动配置原理
- [x] 配置管理和外部化配置
- [x] 日志系统配置

### 第二阶段：Web开发核心
- [x] RESTful API设计
- [x] 参数处理和验证
- [x] 异常处理和全局异常处理
- [x] 数据验证和自定义验证器

### 第三阶段：数据访问层
- [x] JPA/Hibernate实体映射
- [x] Spring Data JPA Repository
- [x] 数据库连接池配置
- [x] 事务管理和传播机制

### 第四阶段：高级特性
- [x] AOP面向切面编程
- [x] 缓存机制和策略
- [x] 异步处理和线程池
- [x] 定时任务和Cron表达式

### 第五阶段：微服务与集成
- [ ] Spring Security认证授权
- [ ] Actuator监控和健康检查
- [ ] 测试框架集成
- [ ] Docker容器化

## 🔍 核心知识点

### SpringBoot核心原理
- 自动配置机制
- 条件注解原理
- 启动流程分析
- 内嵌服务器原理

### IoC容器和Bean管理
- Bean生命周期
- 依赖注入机制
- 循环依赖解决
- 作用域管理

### AOP面向切面编程
- 切面、切点、通知
- 动态代理机制
- 切点表达式
- 性能监控切面

### 数据访问层
- JPA实体映射
- 查询方法设计
- 事务管理
- 性能优化

### 缓存机制
- Spring Cache抽象
- 多级缓存设计
- 缓存问题解决
- 性能优化

### 异步处理
- @Async注解使用
- 线程池配置
- CompletableFuture
- 异常处理

### 定时任务
- @Scheduled注解
- Cron表达式
- 任务管理
- 性能监控

## 📝 面试重点

### 基础概念类
1. SpringBoot的自动配置原理
2. Spring的IoC容器工作原理
3. Bean的生命周期
4. 循环依赖解决机制

### 技术实现类
1. AOP的实现原理
2. 事务管理机制
3. 缓存策略设计
4. 异步处理实现

### 性能优化类
1. 应用性能优化
2. 数据库查询优化
3. 缓存问题解决
4. 系统架构优化

### 架构设计类
1. RESTful API设计
2. 微服务架构设计
3. 分布式系统设计
4. 高可用系统设计

## 🛠️ 技术栈

- **框架**：SpringBoot 3.2.0
- **数据库**：H2、MySQL
- **缓存**：Redis、Spring Cache
- **构建工具**：Maven
- **测试框架**：JUnit 5
- **监控**：Spring Boot Actuator
- **文档**：Markdown

## 📚 学习资源

### 官方文档
- [SpringBoot官方文档](https://spring.io/projects/spring-boot)
- [Spring官方文档](https://spring.io/projects/spring-framework)

### 推荐书籍
- 《Spring Boot实战》
- 《Spring源码深度解析》
- 《Spring Boot微服务实战》

### 在线资源
- [Spring Boot Guides](https://spring.io/guides)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

## 🤝 贡献指南

欢迎提交Issue和Pull Request来完善这个学习项目。

### 贡献方式
1. Fork项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 📄 许可证

本项目采用MIT许可证，详情请参见[LICENSE](LICENSE)文件。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 邮箱：your-email@example.com
- GitHub：your-github-username

---

**祝学习愉快，面试顺利！** 🎉
