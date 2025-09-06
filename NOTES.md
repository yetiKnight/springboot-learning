# SpringBoot 系统性学习笔记

## 📚 学习目标
通过项目实践的方式，系统性地复习SpringBoot核心知识点，为高级开发工程师面试做准备。

## 🎯 面试重点知识点

### 1. SpringBoot 核心原理

#### 1.1 @SpringBootApplication 注解解析
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration  // 等同于@Configuration
@EnableAutoConfiguration  // 启用自动配置
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication
```

**面试重点：**
- `@SpringBootConfiguration`: 标识这是一个配置类
- `@EnableAutoConfiguration`: 核心注解，启用自动配置机制
- `@ComponentScan`: 组件扫描，默认扫描当前包及子包

#### 1.2 自动配置原理
1. **条件注解机制**：`@ConditionalOnClass`、`@ConditionalOnMissingBean`等
2. **配置类加载**：通过`spring.factories`文件加载自动配置类
3. **条件判断**：根据classpath中的类、Bean的存在性等条件决定是否生效

#### 1.3 启动流程
1. 创建`SpringApplication`实例
2. 加载`ApplicationContextInitializer`和`ApplicationListener`
3. 运行`run()`方法
4. 创建`ConfigurableEnvironment`
5. 准备`ConfigurableApplicationContext`
6. 刷新应用上下文
7. 执行`ApplicationRunner`和`CommandLineRunner`

### 2. IoC容器和Bean管理

#### 2.1 Bean的生命周期
1. **实例化**：通过反射创建Bean实例
2. **属性注入**：通过setter方法或字段注入依赖
3. **Aware接口回调**：`BeanNameAware`、`BeanFactoryAware`等
4. **BeanPostProcessor前置处理**：`postProcessBeforeInitialization`
5. **初始化方法**：`@PostConstruct`、`InitializingBean.afterPropertiesSet()`
6. **BeanPostProcessor后置处理**：`postProcessAfterInitialization`
7. **使用Bean**
8. **销毁方法**：`@PreDestroy`、`DisposableBean.destroy()`

#### 2.2 循环依赖解决
**三级缓存机制：**
- `singletonObjects`：一级缓存，存放完全初始化好的Bean
- `earlySingletonObjects`：二级缓存，存放早期暴露的Bean
- `singletonFactories`：三级缓存，存放Bean工厂

**解决过程：**
1. A创建过程中需要B，将A放入三级缓存
2. B创建过程中需要A，从三级缓存获取A的早期引用
3. B创建完成，放入一级缓存
4. A获取B的完整实例，完成创建

### 3. AOP面向切面编程

#### 3.1 核心概念
- **切面(Aspect)**：横切关注点的模块化
- **连接点(Join Point)**：程序执行的某个特定位置
- **切点(Pointcut)**：连接点的集合
- **通知(Advice)**：在切点执行的代码
- **目标对象(Target)**：被代理的对象
- **代理(Proxy)**：AOP框架创建的对象

#### 3.2 实现原理
- **JDK动态代理**：基于接口，使用`Proxy.newProxyInstance()`
- **CGLIB代理**：基于继承，使用字节码技术

### 4. 数据访问层

#### 4.1 Spring Data JPA
- **Repository接口**：提供数据访问抽象
- **查询方法**：通过方法名自动生成查询
- **@Query注解**：自定义JPQL或原生SQL
- **分页和排序**：`Pageable`和`Sort`接口

#### 4.2 事务管理
- **@Transactional注解**：声明式事务
- **事务传播机制**：7种传播行为
- **事务隔离级别**：4种隔离级别
- **回滚规则**：指定哪些异常需要回滚

### 5. Web层开发

#### 5.1 RESTful API设计
- **资源定位**：使用URL定位资源
- **HTTP方法**：GET、POST、PUT、DELETE等
- **状态码**：200、201、400、404、500等
- **内容协商**：支持JSON、XML等格式

#### 5.2 参数处理
- **@RequestParam**：处理查询参数
- **@PathVariable**：处理路径变量
- **@RequestBody**：处理请求体
- **@RequestHeader**：处理请求头

#### 5.3 异常处理
- **@ControllerAdvice**：全局异常处理
- **@ExceptionHandler**：处理特定异常
- **ResponseEntity**：返回自定义响应

### 6. 缓存机制

#### 6.1 Spring Cache抽象
- **@Cacheable**：缓存方法结果
- **@CacheEvict**：清除缓存
- **@CachePut**：更新缓存
- **@Caching**：组合多个缓存注解

#### 6.2 Redis集成
- **连接池配置**：Lettuce连接池参数
- **序列化策略**：JSON、JDK、String序列化
- **过期策略**：TTL、LRU等

### 7. 安全认证

#### 7.1 Spring Security
- **认证(Authentication)**：验证用户身份
- **授权(Authorization)**：控制访问权限
- **密码加密**：BCrypt等加密算法
- **JWT令牌**：无状态认证方案

### 8. 监控和运维

#### 8.1 Actuator
- **健康检查**：`/actuator/health`
- **指标监控**：`/actuator/metrics`
- **环境信息**：`/actuator/env`
- **配置信息**：`/actuator/configprops`

#### 8.2 性能优化
- **连接池调优**：HikariCP参数优化
- **JVM参数调优**：堆内存、GC策略
- **缓存策略**：多级缓存、缓存穿透防护
- **数据库优化**：索引、查询优化

## 📝 学习进度

- [x] 项目初始化和基础配置
- [ ] 核心注解和自动配置原理
- [ ] Web层开发实践
- [ ] 数据访问层实现
- [ ] 高级特性应用
- [ ] 安全认证集成
- [ ] 微服务相关技术
- [ ] 性能优化和监控

## 🔍 面试题库

### 基础概念类
1. SpringBoot的自动配置原理是什么？
2. Spring的IoC容器是如何工作的？
3. Bean的生命周期包括哪些阶段？
4. 如何解决循环依赖问题？

### 技术实现类
1. AOP的实现原理是什么？
2. Spring事务管理是如何工作的？
3. 如何实现分布式缓存？
4. Spring Security的认证流程是什么？

### 性能优化类
1. 如何优化SpringBoot应用的性能？
2. 数据库连接池如何调优？
3. 如何避免N+1查询问题？
4. 缓存穿透、缓存雪崩如何解决？

### 架构设计类
1. 如何设计RESTful API？
2. 微服务架构中如何保证数据一致性？
3. 如何实现分布式锁？
4. 如何设计高可用的系统架构？
