# SpringBoot 面试题库

## 🎯 基础概念类问题

### 1. SpringBoot的自动配置原理是什么？

**标准答案：**
SpringBoot的自动配置基于条件注解和配置类加载机制实现：

1. **@EnableAutoConfiguration注解**：启动自动配置
2. **spring.factories文件**：定义自动配置类列表
3. **条件注解**：根据条件决定是否生效
4. **配置类加载**：通过反射加载配置类

**深入理解：**
```java
// 1. 启动时扫描META-INF/spring.factories
// 2. 加载所有自动配置类
// 3. 根据条件注解判断是否生效
// 4. 创建Bean并注册到IoC容器

@ConditionalOnClass(DataSource.class)  // 类路径存在DataSource
@ConditionalOnMissingBean(DataSource.class)  // 容器中不存在DataSource Bean
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")  // 配置属性存在
```

**面试技巧：**
- 能够画出自动配置的流程图
- 举例说明具体的自动配置类
- 解释条件注解的工作原理

### 2. Spring的IoC容器是如何工作的？

**标准答案：**
IoC容器通过BeanDefinition管理Bean的生命周期：

1. **BeanDefinition注册**：解析配置，注册Bean定义
2. **Bean实例化**：通过反射创建Bean实例
3. **依赖注入**：通过setter、构造器或字段注入依赖
4. **初始化**：调用初始化方法
5. **使用Bean**：从容器中获取Bean使用

**核心机制：**
```java
// 三级缓存解决循环依赖
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);  // 一级缓存
private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);        // 二级缓存
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16); // 三级缓存
```

**面试技巧：**
- 能够详细描述Bean的创建过程
- 解释循环依赖的解决机制
- 说明不同作用域Bean的处理方式

### 3. Bean的生命周期包括哪些阶段？

**完整生命周期：**
1. **实例化**：通过反射创建Bean实例
2. **属性注入**：通过setter方法或字段注入依赖
3. **Aware接口回调**：`BeanNameAware`、`BeanFactoryAware`、`ApplicationContextAware`
4. **BeanPostProcessor前置处理**：`postProcessBeforeInitialization`
5. **初始化方法**：
   - `@PostConstruct` 注解方法
   - `InitializingBean.afterPropertiesSet()`
   - 自定义init-method
6. **BeanPostProcessor后置处理**：`postProcessAfterInitialization`
7. **使用Bean**
8. **销毁方法**：
   - `@PreDestroy` 注解方法
   - `DisposableBean.destroy()`
   - 自定义destroy-method

**面试技巧：**
- 能够画出Bean生命周期的时序图
- 举例说明每个阶段的具体作用
- 解释BeanPostProcessor的作用

### 4. 如何解决循环依赖问题？

**解决方案：**
- **构造器循环依赖**：无法解决，抛出异常
- **setter循环依赖**：通过三级缓存机制解决
- **prototype循环依赖**：无法解决，抛出异常

**三级缓存解决过程：**
```java
// A依赖B，B依赖A的情况
// 1. A开始创建，放入三级缓存
singletonFactories.put("a", () -> getEarlyBeanReference("a", mbd, a));

// 2. A需要B，开始创建B
// 3. B需要A，从三级缓存获取A的早期引用
Object earlySingletonReference = getSingleton("a", false);

// 4. B创建完成，放入一级缓存
// 5. A获取B的完整实例，完成创建
```

**面试技巧：**
- 能够详细描述三级缓存的工作过程
- 解释为什么构造器循环依赖无法解决
- 说明不同作用域Bean的循环依赖处理

## 🔧 技术实现类问题

### 5. AOP的实现原理是什么？

**核心概念：**
- **切面(Aspect)**：横切关注点的模块化
- **连接点(Join Point)**：程序执行的某个特定位置
- **切点(Pointcut)**：连接点的集合
- **通知(Advice)**：在切点执行的代码

**实现方式：**
1. **JDK动态代理**：基于接口，使用`Proxy.newProxyInstance()`
2. **CGLIB代理**：基于继承，使用字节码技术

**切点表达式：**
```java
@Pointcut("execution(* com.learning.service.*.*(..))")  // 方法执行
@Pointcut("within(com.learning.service.*)")  // 类内部
@Pointcut("this(com.learning.service.UserService)")  // 代理对象
@Pointcut("target(com.learning.service.UserService)")  // 目标对象
```

**面试技巧：**
- 能够解释AOP的核心概念
- 说明JDK动态代理和CGLIB的区别
- 举例说明切点表达式的写法

### 6. Spring事务管理是如何工作的？

**事务管理方式：**
1. **编程式事务**：使用`TransactionTemplate`
2. **声明式事务**：使用`@Transactional`注解

**事务传播机制：**
- `REQUIRED`：默认，如果存在事务则加入，否则创建新事务
- `REQUIRES_NEW`：总是创建新事务
- `SUPPORTS`：如果存在事务则加入，否则以非事务方式执行
- `NOT_SUPPORTED`：以非事务方式执行
- `MANDATORY`：必须在事务中执行
- `NEVER`：不能在事务中执行
- `NESTED`：嵌套事务

**事务隔离级别：**
- `READ_UNCOMMITTED`：读未提交
- `READ_COMMITTED`：读已提交
- `REPEATABLE_READ`：可重复读
- `SERIALIZABLE`：串行化

**面试技巧：**
- 能够解释事务传播机制的使用场景
- 说明不同隔离级别的特点
- 举例说明事务回滚的条件

### 7. 如何实现分布式缓存？

**缓存策略：**
1. **本地缓存**：Caffeine、Guava Cache
2. **分布式缓存**：Redis、Hazelcast
3. **多级缓存**：本地缓存 + 分布式缓存

**Spring Cache抽象：**
```java
@Cacheable(value = "users", key = "#id")  // 缓存方法结果
@CacheEvict(value = "users", allEntries = true)  // 清除缓存
@CachePut(value = "users", key = "#user.id")  // 更新缓存
@Caching(  // 组合多个缓存注解
    cacheable = @Cacheable(value = "users", key = "#id"),
    evict = @CacheEvict(value = "users", allEntries = true)
)
```

**缓存问题解决：**
- **缓存穿透**：布隆过滤器、空值缓存
- **缓存雪崩**：随机过期时间、熔断降级
- **缓存击穿**：分布式锁、热点数据预热

**面试技巧：**
- 能够解释不同缓存策略的优缺点
- 说明缓存问题的解决方案
- 举例说明缓存性能优化的方法

### 8. Spring Security的认证流程是什么？

**认证流程：**
1. 用户提交用户名和密码
2. `AuthenticationManager` 处理认证请求
3. `UserDetailsService` 加载用户信息
4. `PasswordEncoder` 验证密码
5. 认证成功后生成`Authentication`对象
6. 存储到`SecurityContext`中

**JWT认证实现：**
```java
// 1. 用户登录，验证用户名密码
// 2. 生成JWT Token
String token = Jwts.builder()
    .setSubject(username)
    .setExpiration(new Date(System.currentTimeMillis() + expiration))
    .signWith(SignatureAlgorithm.HS512, secret)
    .compact();

// 3. 返回Token给客户端
// 4. 客户端在请求头中携带Token
// 5. 服务端验证Token并设置认证信息
```

**面试技巧：**
- 能够画出认证流程图
- 说明JWT Token的生成和验证过程
- 解释Session和Token的区别

## 🚀 性能优化类问题

### 9. 如何优化SpringBoot应用的性能？

**JVM层面：**
- 堆内存调优：`-Xms`、`-Xmx`
- GC策略选择：G1GC、ZGC
- 线程栈大小：`-Xss`

**应用层面：**
- 连接池调优：HikariCP参数优化
- 缓存策略：多级缓存、缓存预热
- 异步处理：`@Async`、线程池配置
- 数据库优化：索引、查询优化

**连接池参数调优：**
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20      # 最大连接数
      minimum-idle: 5            # 最小空闲连接数
      connection-timeout: 30000  # 连接超时时间
      idle-timeout: 600000       # 空闲连接超时时间
      max-lifetime: 1800000      # 连接最大生存时间
      leak-detection-threshold: 60000  # 连接泄漏检测阈值
```

**面试技巧：**
- 能够从多个层面分析性能问题
- 说明性能监控的方法
- 举例说明具体的优化措施

### 10. 如何避免N+1查询问题？

**问题描述：**
查询1个用户，需要查询N个订单，产生1+N次数据库查询

**解决方案：**
1. **使用JOIN FETCH**：
```java
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findByIdWithOrders(@Param("id") Long id);
```

2. **使用@BatchSize注解**：
```java
@OneToMany(mappedBy = "user")
@BatchSize(size = 10)
private List<Order> orders;
```

3. **使用@NamedEntityGraph**：
```java
@Entity
@NamedEntityGraph(
    name = "User.withOrders",
    attributeNodes = @NamedAttributeNode("orders")
)
public class User { ... }
```

**面试技巧：**
- 能够解释N+1问题的产生原因
- 说明不同解决方案的优缺点
- 举例说明具体的实现方法

### 11. 缓存穿透、缓存雪崩如何解决？

**缓存穿透：**
- 问题：查询不存在的数据，绕过缓存直接查询数据库
- 解决：布隆过滤器、空值缓存、参数校验

**缓存雪崩：**
- 问题：大量缓存同时过期，导致请求直接打到数据库
- 解决：随机过期时间、熔断降级、缓存预热

**缓存击穿：**
- 问题：热点数据过期，大量请求同时查询数据库
- 解决：分布式锁、热点数据永不过期

**面试技巧：**
- 能够区分三种缓存问题
- 说明每种问题的解决方案
- 举例说明具体的实现方法

## 🏗️ 架构设计类问题

### 12. 如何设计RESTful API？

**设计原则：**
- 使用HTTP方法表示操作：GET、POST、PUT、DELETE
- 使用名词表示资源：`/users`、`/orders`
- 使用复数形式：`/users`而不是`/user`
- 使用层级结构：`/users/{id}/orders`
- 使用查询参数：`/users?status=active&page=1`

**HTTP状态码：**
- `200 OK`：请求成功
- `201 Created`：资源创建成功
- `400 Bad Request`：请求参数错误
- `401 Unauthorized`：未认证
- `403 Forbidden`：无权限
- `404 Not Found`：资源不存在
- `500 Internal Server Error`：服务器内部错误

**面试技巧：**
- 能够设计完整的API接口
- 说明RESTful设计的原则
- 举例说明状态码的使用

### 13. 微服务架构中如何保证数据一致性？

**解决方案：**
1. **分布式事务**：2PC、3PC、TCC、Saga
2. **最终一致性**：事件驱动、消息队列
3. **补偿机制**：重试、回滚、人工干预

**Saga模式实现：**
```java
// 1. 订单服务创建订单
// 2. 库存服务扣减库存
// 3. 支付服务处理支付
// 4. 如果任何步骤失败，执行补偿操作
```

**面试技巧：**
- 能够解释不同一致性方案的优缺点
- 说明Saga模式的工作原理
- 举例说明具体的实现方法

### 14. 如何实现分布式锁？

**实现方式：**
1. **Redis分布式锁**：SET NX EX
2. **Zookeeper分布式锁**：临时顺序节点
3. **数据库分布式锁**：唯一索引

**Redis分布式锁实现：**
```java
// 加锁
String result = jedis.set(key, value, "NX", "EX", expireTime);
if ("OK".equals(result)) {
    // 获取锁成功
}

// 解锁（使用Lua脚本保证原子性）
String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
```

**面试技巧：**
- 能够解释不同分布式锁的实现原理
- 说明分布式锁的注意事项
- 举例说明具体的实现方法

### 15. 如何设计高可用的系统架构？

**高可用设计原则：**
1. **无单点故障**：负载均衡、集群部署
2. **故障隔离**：熔断器、限流器
3. **快速恢复**：健康检查、自动重启
4. **数据备份**：主从复制、定期备份

**架构组件：**
- **负载均衡**：Nginx、HAProxy
- **服务注册发现**：Eureka、Consul、Nacos
- **配置中心**：Apollo、Nacos
- **消息队列**：RabbitMQ、Kafka
- **监控告警**：Prometheus、Grafana
- **链路追踪**：Zipkin、Jaeger

**面试技巧：**
- 能够设计完整的系统架构
- 说明高可用的设计原则
- 举例说明具体的实现方案

## 📝 实践建议

### 学习路径建议：
1. **基础阶段**：掌握Spring核心概念和注解
2. **进阶阶段**：深入理解AOP、事务、缓存等机制
3. **高级阶段**：学习微服务、分布式系统设计
4. **实战阶段**：参与大型项目，积累经验

### 面试准备建议：
1. **理论结合实践**：每个知识点都要有代码示例
2. **深入理解原理**：不仅要知道怎么用，更要知道为什么
3. **关注最新动态**：了解SpringBoot的最新特性和发展趋势
4. **准备项目案例**：能够详细描述项目中的技术选型和实现方案

### 常见面试问题：
1. 描述一下SpringBoot的启动过程
2. 解释一下Spring的IoC和AOP
3. 如何解决循环依赖问题？
4. 事务的传播机制有哪些？
5. 如何优化数据库查询性能？
6. 微服务架构的优缺点是什么？
7. 如何保证分布式系统的一致性？
8. 描述一下你参与过的最复杂的项目

### 面试技巧：
1. **回答问题要有层次**：先回答核心概念，再深入细节
2. **举例说明**：每个概念都要有具体的代码示例
3. **画图说明**：复杂的概念可以用图表来辅助说明
4. **承认不足**：不知道的问题要诚实回答，不要猜测
5. **展示学习能力**：说明如何学习和掌握新技术

### 项目经验准备：
1. **技术选型**：为什么选择这些技术？
2. **架构设计**：如何设计系统架构？
3. **性能优化**：如何优化系统性能？
4. **问题解决**：遇到问题如何解决？
5. **团队协作**：如何与团队协作？

### 代码能力展示：
1. **代码质量**：代码规范、注释完整
2. **设计模式**：能够使用常见的设计模式
3. **异常处理**：完善的异常处理机制
4. **测试覆盖**：单元测试、集成测试
5. **性能考虑**：代码的性能优化

记住：面试不仅仅是技术能力的考察，更是沟通能力、学习能力、解决问题的能力的综合体现。保持自信，诚实回答，展示你的学习能力和解决问题的思路。
