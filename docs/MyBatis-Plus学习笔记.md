# MyBatis Plus 学习笔记

## 📚 概述

MyBatis Plus 是一个 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

## 🎯 核心特性

### 1. 无侵入性
- 只做增强不做改变，引入它不会对现有工程产生影响
- 如丝般顺滑的切换

### 2. 损耗小
- 启动即会自动注入基本 CRUD，性能基本无损耗，直接面向对象操作

### 3. 强大的 CRUD 操作
- 内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作
- 更有强大的条件构造器，满足各类使用需求

### 4. 支持 Lambda 形式调用
- 通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错

### 5. 支持主键自动生成
- 支持多达 4 种主键策略（包含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题

### 6. 支持 ActiveRecord 模式
- 支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作

### 7. 支持自定义全局通用操作
- 支持全局通用方法注入（ Write once, use anywhere ）

### 8. 内置代码生成器
- 采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码
- 支持模板引擎，更有超多自定义配置等您来使用

### 9. 内置分页插件
- 基于 MyBatis 物理分页，开发者无需关心具体操作
- 分页插件支持多种数据库：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库

### 10. 分页插件支持多种数据库
- 支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库

### 11. 内置性能分析插件
- 可输出 SQL 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询

### 12. 内置全局拦截插件
- 提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

## 🏗️ 项目中的实现

### 1. 依赖配置

```xml
<!-- MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.4</version>
</dependency>
```

### 2. 配置文件

```yaml
# MyBatis Plus配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    jdbc-type-for-null: 'null'
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.learning.entity
```

### 3. 实体类配置

```java
@TableName("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("username")
    private String username;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;
}
```

### 4. Mapper接口

```java
@Mapper
public interface UserRepository extends BaseMapper<User> {
    
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);
}
```

### 5. Service层

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.selectById(id);
    }
    
    public List<User> findByCondition(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (user.getUsername() != null) {
            queryWrapper.like("username", user.getUsername());
        }
        
        return userRepository.selectList(queryWrapper);
    }
}
```

## 🔧 核心注解

### 1. @TableName
- 作用：指定实体类对应的数据库表名
- 使用场景：当实体类名与数据库表名不一致时

### 2. @TableId
- 作用：指定主键字段
- 属性：
  - `type`：主键类型（AUTO、NONE、INPUT、ASSIGN_ID、ASSIGN_UUID）
  - `value`：主键字段名

### 3. @TableField
- 作用：指定非主键字段
- 属性：
  - `value`：数据库字段名
  - `fill`：自动填充策略（INSERT、UPDATE、INSERT_UPDATE）
  - `exist`：是否为数据库表字段

### 4. @TableLogic
- 作用：逻辑删除字段
- 配合全局配置实现逻辑删除

### 5. @Version
- 作用：乐观锁字段
- 用于实现乐观锁机制

## 🚀 核心功能

### 1. 条件构造器

```java
// 查询条件构造
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
queryWrapper.eq("status", "ACTIVE")
           .like("username", "admin")
           .between("created_at", startDate, endDate)
           .orderByDesc("created_at");

List<User> users = userRepository.selectList(queryWrapper);
```

### 2. Lambda条件构造器

```java
// Lambda查询条件构造
LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
lambdaQuery.eq(User::getStatus, "ACTIVE")
          .like(User::getUsername, "admin")
          .between(User::getCreatedAt, startDate, endDate);

List<User> users = userRepository.selectList(lambdaQuery);
```

### 3. 分页查询

```java
// 分页查询
Page<User> page = new Page<>(1, 10);
IPage<User> result = userRepository.selectPage(page, queryWrapper);
```

### 4. 自动填充

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}
```

## 📊 性能优化

### 1. 分页插件配置

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    
    // 分页插件
    PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.H2);
    paginationInterceptor.setMaxLimit(1000L);
    paginationInterceptor.setOverflow(false);
    
    interceptor.addInnerInterceptor(paginationInterceptor);
    return interceptor;
}
```

### 2. 逻辑删除配置

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 3. 字段映射配置

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
```

## 🎯 面试重点

### 1. MyBatis Plus与MyBatis的区别
- MyBatis Plus在MyBatis基础上做了增强
- 提供了通用的CRUD操作
- 内置了分页插件、代码生成器等

### 2. 条件构造器的使用
- QueryWrapper：字符串字段名
- LambdaQueryWrapper：Lambda表达式，类型安全

### 3. 分页插件的原理
- 基于MyBatis的物理分页
- 自动识别数据库类型
- 支持多种数据库

### 4. 自动填充的实现
- 通过MetaObjectHandler接口
- 支持插入和更新时的自动填充
- 可以自定义填充策略

### 5. 逻辑删除的实现
- 通过@TableLogic注解
- 全局配置逻辑删除字段
- 查询时自动过滤已删除数据

## 🔍 最佳实践

### 1. 实体类设计
- 使用@TableName指定表名
- 使用@TableId指定主键
- 使用@TableField指定字段映射
- 使用@TableLogic实现逻辑删除

### 2. Mapper接口设计
- 继承BaseMapper获得基础CRUD能力
- 自定义方法使用@Select等注解
- 复杂查询使用XML映射文件

### 3. Service层设计
- 使用条件构造器构建查询条件
- 合理使用分页查询
- 注意事务管理

### 4. 性能优化
- 合理使用索引
- 避免N+1查询问题
- 使用分页查询处理大数据量
- 合理配置缓存

## 📚 学习资源

### 官方文档
- [MyBatis Plus官方文档](https://baomidou.com/)
- [MyBatis Plus GitHub](https://github.com/baomidou/mybatis-plus)

### 推荐书籍
- 《MyBatis从入门到精通》
- 《Spring Boot实战》

### 在线资源
- [MyBatis Plus官方示例](https://github.com/baomidou/mybatis-plus-samples)
- [MyBatis Plus代码生成器](https://baomidou.com/pages/779a6e/)
