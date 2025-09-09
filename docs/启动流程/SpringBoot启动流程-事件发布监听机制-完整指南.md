# SpringBoot启动流程-事件发布监听机制-完整指南

## 1. 事件发布监听机制概述

SpringBoot的事件发布监听机制是启动流程中的重要组成部分，它基于Spring Framework的观察者模式实现，允许在应用启动的不同阶段发布事件，并支持多个监听器同时响应这些事件。

### 1.1 核心组件

- **ApplicationEvent**: 应用事件抽象类
- **ApplicationEventPublisher**: 事件发布器接口
- **ApplicationEventMulticaster**: 事件多播器接口
- **ApplicationListener**: 事件监听器接口
- **SpringApplicationRunListener**: SpringBoot启动监听器接口

### 1.2 事件发布流程时序图

```mermaid
sequenceDiagram
    participant SA as SpringApplication
    participant EPRL as EventPublishingRunListener
    participant AEM as ApplicationEventMulticaster
    participant AL as ApplicationListener
    participant AC as ApplicationContext

    Note over SA,AC: SpringBoot启动流程中的事件发布监听机制
    Note over SA,AC: 基于观察者模式，实现松耦合的事件通信

    rect rgb(240, 248, 255)
        Note over SA,AL: 阶段1: 应用启动开始
        SA->>EPRL: starting()<br/>调用启动监听器
        Note right of EPRL: 应用开始启动，<br/>环境还未准备
        EPRL->>AEM: multicastEvent(ApplicationStartingEvent)<br/>发布启动事件
        Note right of AEM: 事件多播器负责<br/>分发事件给监听器
        AEM->>AL: onApplicationEvent()<br/>通知所有监听器
        Note right of AL: 监听器处理启动事件
    end

    rect rgb(240, 255, 240)
        Note over SA,AL: 阶段2: 环境准备完成
        SA->>EPRL: environmentPrepared()<br/>环境准备完成回调
        Note right of EPRL: 环境配置加载完成，<br/>但上下文还未创建
        EPRL->>AEM: multicastEvent(ApplicationEnvironmentPreparedEvent)<br/>发布环境准备事件
        AEM->>AL: onApplicationEvent()<br/>通知环境准备监听器
        Note right of AL: 监听器可以访问环境配置
    end

    rect rgb(255, 248, 240)
        Note over SA,AL: 阶段3: 上下文准备完成
        SA->>EPRL: contextPrepared()<br/>上下文准备完成回调
        Note right of EPRL: 上下文创建完成，<br/>但还未刷新
        EPRL->>AEM: multicastEvent(ApplicationContextPreparedEvent)<br/>发布上下文准备事件
        AEM->>AL: onApplicationEvent()<br/>通知上下文准备监听器
        Note right of AL: 监听器可以访问上下文
    end

    rect rgb(255, 240, 255)
        Note over SA,AL: 阶段4: 上下文加载完成
        SA->>EPRL: contextLoaded()<br/>上下文加载完成回调
        Note right of EPRL: 上下文刷新完成，<br/>Bean定义加载完成
        EPRL->>AEM: multicastEvent(ApplicationContextLoadedEvent)<br/>发布上下文加载事件
        AEM->>AL: onApplicationEvent()<br/>通知上下文加载监听器
        Note right of AL: 监听器可以访问所有Bean
    end

    rect rgb(248, 255, 248)
        Note over SA,AL: 阶段5: 应用启动完成
        SA->>EPRL: started()<br/>应用启动完成回调
        Note right of EPRL: 所有Bean初始化完成，<br/>但还未调用Runner
        EPRL->>AC: publishEvent(ApplicationStartedEvent)<br/>通过上下文发布启动完成事件
        Note right of AC: 此时使用ApplicationContext<br/>作为事件发布器
        AC->>AL: onApplicationEvent()<br/>通知启动完成监听器
        Note right of AL: 监听器可以执行启动后逻辑
    end

    rect rgb(255, 255, 240)
        Note over SA,AL: 阶段6: 应用就绪
        SA->>EPRL: running()<br/>应用运行中回调
        Note right of EPRL: 所有启动任务完成，<br/>应用可以接收请求
        EPRL->>AC: publishEvent(ApplicationReadyEvent)<br/>发布应用就绪事件
        AC->>AL: onApplicationEvent()<br/>通知应用就绪监听器
        Note right of AL: 监听器执行最终初始化逻辑
    end
```

## 2. 事件体系结构

### 2.1 事件体系架构图

```mermaid
graph TB
    A[ApplicationEvent] --> B[SpringApplicationEvent]
    B --> C[ApplicationStartingEvent]
    B --> D[ApplicationEnvironmentPreparedEvent]
    B --> E[ApplicationContextPreparedEvent]
    B --> F[ApplicationContextLoadedEvent]
    B --> G[ApplicationStartedEvent]
    B --> H[ApplicationReadyEvent]
    B --> I[ApplicationFailedEvent]
    
    J[ApplicationEventPublisher] --> K[AbstractApplicationContext]
    L[ApplicationEventMulticaster] --> M[SimpleApplicationEventMulticaster]
    N[ApplicationListener] --> O[SpringApplicationRunListener]
    O --> P[EventPublishingRunListener]
    
    K --> L
    M --> N
```

### 2.2 SpringBoot启动事件类型

| 事件类型 | 发布时机 | 说明 |
|---------|---------|------|
| `ApplicationStartingEvent` | 应用开始启动 | 最早发布的事件，此时环境还未准备 |
| `ApplicationEnvironmentPreparedEvent` | 环境准备完成 | 环境配置加载完成，但上下文还未创建 |
| `ApplicationContextPreparedEvent` | 上下文准备完成 | 上下文创建完成，但还未刷新 |
| `ApplicationContextLoadedEvent` | 上下文加载完成 | 上下文刷新完成，Bean定义加载完成 |
| `ApplicationStartedEvent` | 应用启动完成 | 所有Bean初始化完成，但还未调用Runner |
| `ApplicationReadyEvent` | 应用就绪 | 所有启动任务完成，应用可以接收请求 |
| `ApplicationFailedEvent` | 启动失败 | 任何阶段发生异常时发布 |

### 2.3 事件发布流程图

```mermaid
flowchart TD
    A[SpringApplication.run] --> B[获取RunListeners]
    B --> C[发布ApplicationStartingEvent]
    C --> D[准备环境]
    D --> E[发布ApplicationEnvironmentPreparedEvent]
    E --> F[创建ApplicationContext]
    F --> G[发布ApplicationContextPreparedEvent]
    G --> H[刷新ApplicationContext]
    H --> I[发布ApplicationContextLoadedEvent]
    I --> J[调用ApplicationRunner/CommandLineRunner]
    J --> K[发布ApplicationStartedEvent]
    K --> L[发布ApplicationReadyEvent]
    
    M[异常发生] --> N[发布ApplicationFailedEvent]
    C --> M
    D --> M
    E --> M
    F --> M
    G --> M
    H --> M
    I --> M
    J --> M
    K --> M
```

## 3. 事件发布器体系

### 3.1 事件发布器架构

```mermaid
graph LR
    A[ApplicationEventPublisher] --> B[AbstractApplicationContext]
    B --> C[ApplicationEventMulticaster]
    C --> D[SimpleApplicationEventMulticaster]
    D --> E[ApplicationListener]
    
    F[EventPublishingRunListener] --> G[SimpleApplicationEventMulticaster]
    G --> H[ApplicationListener]
```

### 3.2 核心接口

**ApplicationEventPublisher接口**:

```java
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
    void publishEvent(Object event);
}
```

**事件发布流程**:

1. 事件发布器接收事件
2. 将事件传递给事件多播器
3. 多播器查找匹配的监听器
4. 调用监听器的处理方法

## 4. 事件多播器机制

### 4.1 多播器工作流程

```mermaid
flowchart TD
    A[接收事件] --> B[解析事件类型]
    B --> C[查找匹配的监听器]
    C --> D{是否有监听器?}
    D -->|是| E[遍历监听器]
    D -->|否| F[结束]
    E --> G{是否异步?}
    G -->|是| H[异步执行]
    G -->|否| I[同步执行]
    H --> J[调用监听器方法]
    I --> J
    J --> K{还有监听器?}
    K -->|是| E
    K -->|否| F

    %% 添加详细注释
    A -.->|注释| A1[ApplicationEventMulticaster<br/>接收事件对象]
    B -.->|注释| B1[根据事件类型<br/>确定监听器范围]
    C -.->|注释| C1[从监听器缓存中<br/>查找匹配的监听器]
    D -.->|注释| D1[检查是否有<br/>注册的监听器]
    E -.->|注释| E1[按Order顺序<br/>遍历监听器列表]
    G -.->|注释| G1[检查监听器是否<br/>配置为异步执行]
    H -.->|注释| H1[提交到线程池<br/>异步执行监听器]
    I -.->|注释| I1[在当前线程<br/>同步执行监听器]
    J -.->|注释| J1[调用监听器的<br/>onApplicationEvent方法]
    K -.->|注释| K1[检查是否还有<br/>其他监听器需要执行]
    F -.->|注释| F1[所有监听器执行完成<br/>事件处理结束]

    %% 样式定义
    classDef processBox fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef decisionBox fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef endBox fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef commentBox fill:#f5f5f5,stroke:#616161,stroke-width:1px,stroke-dasharray: 5 5

    class A,B,C,E,H,I,J processBox
    class D,G,K decisionBox
    class F endBox
    class A1,B1,C1,D1,E1,G1,H1,I1,J1,K1,F1 commentBox
```

### 4.2 核心功能

**ApplicationEventMulticaster接口**:

```java
public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> listener);
    void removeApplicationListener(ApplicationListener<?> listener);
    void multicastEvent(ApplicationEvent event);
}
```

**关键特性**:

- **监听器管理**: 动态添加/移除监听器
- **事件分发**: 将事件分发给所有匹配的监听器
- **异步支持**: 支持异步事件处理
- **异常处理**: 提供异常处理机制
- **缓存优化**: 缓存监听器查找结果

## 5. SpringBoot启动监听器

### 5.1 SpringApplicationRunListener接口

```java
public interface SpringApplicationRunListener {
    void starting(ConfigurableBootstrapContext bootstrapContext);
    void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment);
    void contextPrepared(ConfigurableApplicationContext context);
    void contextLoaded(ConfigurableApplicationContext context);
    void started(ConfigurableApplicationContext context);
    void running(ConfigurableApplicationContext context);
    void failed(ConfigurableApplicationContext context, Throwable exception);
}
```

### 5.2 EventPublishingRunListener核心实现

**关键方法**:

```java
public class EventPublishingRunListener implements SpringApplicationRunListener {
    
    private final SimpleApplicationEventMulticaster initialMulticaster;
    
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        // 发布ApplicationStartingEvent
        this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
    }
    
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, 
            ConfigurableEnvironment environment) {
        // 发布ApplicationEnvironmentPreparedEvent
        this.initialMulticaster.multicastEvent(
                new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
    }
    
    @Override
    public void started(ConfigurableApplicationContext context) {
        // 发布ApplicationStartedEvent
        context.publishEvent(new ApplicationStartedEvent(this.application, this.args, context));
    }
    
    @Override
    public void running(ConfigurableApplicationContext context) {
        // 发布ApplicationReadyEvent
        context.publishEvent(new ApplicationReadyEvent(this.application, this.args, context));
    }
}
```

### 5.3 监听器注册机制

```mermaid
graph TD
    A[SpringApplication] --> B[获取RunListeners]
    B --> C[从spring.factories加载]
    C --> D[创建EventPublishingRunListener]
    D --> E[注册ApplicationListener]
    E --> F[启动事件发布]
```

## 6. 启动事件发布顺序

### 6.1 事件发布时序图

```mermaid
sequenceDiagram
    participant SA as SpringApplication
    participant RL as RunListeners
    participant EPRL as EventPublishingRunListener
    participant AC as ApplicationContext

    Note over SA,AC: SpringBoot启动流程中的事件发布详细时序
    Note over SA,AC: 展示每个阶段的事件发布时机和上下文切换

    rect rgb(240, 248, 255)
        Note over SA,AC: 初始化阶段
        SA->>RL: getRunListeners()<br/>获取所有启动监听器
        Note right of RL: 从spring.factories加载<br/>EventPublishingRunListener等
        SA->>EPRL: starting()<br/>调用启动监听器
        Note right of EPRL: 应用开始启动，<br/>最早的事件发布时机
        EPRL->>EPRL: 发布ApplicationStartingEvent<br/>通过SimpleApplicationEventMulticaster
        Note right of EPRL: 此时使用初始多播器，<br/>ApplicationContext还未创建
    end

    rect rgb(240, 255, 240)
        Note over SA,AC: 环境准备阶段
        SA->>SA: prepareEnvironment()<br/>准备应用环境
        Note right of SA: 加载配置文件、<br/>设置系统属性等
        SA->>EPRL: environmentPrepared()<br/>环境准备完成回调
        Note right of EPRL: 环境配置加载完成，<br/>但上下文还未创建
        EPRL->>EPRL: 发布ApplicationEnvironmentPreparedEvent<br/>环境准备事件
        Note right of EPRL: 监听器可以访问环境配置，<br/>但无法访问Bean
    end

    rect rgb(255, 248, 240)
        Note over SA,AC: 上下文创建阶段
        SA->>SA: createApplicationContext()<br/>创建应用上下文
        Note right of SA: 根据应用类型创建<br/>AnnotationConfigApplicationContext等
        SA->>EPRL: contextPrepared()<br/>上下文准备完成回调
        Note right of EPRL: 上下文创建完成，<br/>但还未刷新
        EPRL->>EPRL: 发布ApplicationContextPreparedEvent<br/>上下文准备事件
        Note right of EPRL: 监听器可以访问上下文，<br/>但Bean还未初始化
    end

    rect rgb(255, 240, 255)
        Note over SA,AC: 上下文刷新阶段
        SA->>SA: refreshContext()<br/>刷新应用上下文
        Note right of SA: 执行Bean定义扫描、<br/>Bean实例化、依赖注入等
        SA->>EPRL: contextLoaded()<br/>上下文加载完成回调
        Note right of EPRL: 上下文刷新完成，<br/>Bean定义加载完成
        EPRL->>EPRL: 发布ApplicationContextLoadedEvent<br/>上下文加载事件
        Note right of EPRL: 监听器可以访问所有Bean，<br/>但Runner还未执行
    end

    rect rgb(248, 255, 248)
        Note over SA,AC: 启动完成阶段
        SA->>EPRL: started()<br/>应用启动完成回调
        Note right of EPRL: 所有Bean初始化完成，<br/>但还未调用Runner
        EPRL->>AC: 发布ApplicationStartedEvent<br/>通过ApplicationContext发布
        Note right of AC: 切换到ApplicationContext<br/>作为事件发布器
        Note right of AC: 此时所有Bean都已就绪，<br/>可以执行复杂的启动逻辑
    end

    rect rgb(255, 255, 240)
        Note over SA,AC: 应用就绪阶段
        SA->>EPRL: running()<br/>应用运行中回调
        Note right of EPRL: 所有启动任务完成，<br/>应用可以接收请求
        EPRL->>AC: 发布ApplicationReadyEvent<br/>应用就绪事件
        Note right of AC: 最终事件，表示应用<br/>完全启动并可以提供服务
        Note right of AC: 监听器可以执行<br/>最终的业务初始化逻辑
    end
```

### 6.2 事件发布顺序表

| 序号 | 事件类型 | 发布时机 | 发布者 |
|------|---------|---------|--------|
| 1 | `ApplicationStartingEvent` | 应用开始启动 | EventPublishingRunListener |
| 2 | `ApplicationEnvironmentPreparedEvent` | 环境准备完成 | EventPublishingRunListener |
| 3 | `ApplicationContextPreparedEvent` | 上下文准备完成 | EventPublishingRunListener |
| 4 | `ApplicationContextLoadedEvent` | 上下文加载完成 | EventPublishingRunListener |
| 5 | `ApplicationStartedEvent` | 应用启动完成 | ApplicationContext |
| 6 | `ApplicationReadyEvent` | 应用就绪 | ApplicationContext |
| 7 | `ApplicationFailedEvent` | 启动失败 | EventPublishingRunListener/ApplicationContext |

## 7. 自定义事件监听器

### 7.1 实现ApplicationListener接口

```java
@Component
public class CustomApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomApplicationListener.class);
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Application is ready! Context: {}", event.getApplicationContext().getDisplayName());
        performCustomLogic();
    }
    
    private void performCustomLogic() {
        logger.info("Performing custom startup logic...");
    }
}
```

### 7.2 使用@EventListener注解

```java
@Component
public class EventListenerComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(EventListenerComponent.class);
    
    @EventListener
    public void handleApplicationStarting(ApplicationStartingEvent event) {
        logger.info("Application is starting...");
    }
    
    @EventListener
    public void handleApplicationReady(ApplicationReadyEvent event) {
        logger.info("Application is ready!");
        executePostStartupLogic();
    }
    
    @EventListener
    public void handleApplicationFailed(ApplicationFailedEvent event) {
        logger.error("Application failed to start: {}", event.getException().getMessage());
        handleStartupFailure(event.getException());
    }
    
    private void executePostStartupLogic() {
        // 启动后的业务逻辑
    }
    
    private void handleStartupFailure(Throwable exception) {
        // 启动失败处理逻辑
    }
}
```

### 7.3 条件化事件监听

```java
@Component
public class ConditionalEventListener {
    
    @EventListener(condition = "#event.environment.getActiveProfiles()[0] == 'dev'")
    public void handleDevEnvironment(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("Development environment detected!");
    }
    
    @EventListener(condition = "#event.environment.getActiveProfiles()[0] == 'prod'")
    public void handleProdEnvironment(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("Production environment detected!");
    }
}
```

## 8. 事件监听器执行顺序控制

### 8.1 执行顺序控制方式

| 方式 | 注解/接口 | 说明 | 示例 |
|------|----------|------|------|
| **@Order** | `@Order(value)` | 数值越小，优先级越高 | `@Order(1)` |
| **Ordered接口** | `implements Ordered` | 实现getOrder()方法 | `getOrder() { return 100; }` |
| **@Priority** | `@Priority(value)` | 数值越小，优先级越高 | `@Priority(1)` |

**注意**：@Order由Spring原生支持，@Priority是JSR标准，在Spring 项目中，两者存在时优先使用@Order

### 8.2 实际应用示例

```java
@Component
@Order(1)
public class FirstEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("First listener executed");
    }
}

@Component
@Order(2)
public class SecondEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Second listener executed");
    }
}

@Component
public class OrderedEventListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Ordered listener executed");
    }
    
    @Override
    public int getOrder() {
        return 100; // 数值越小，优先级越高
    }
}
```

### 8.3 执行顺序流程图

```mermaid
flowchart TD
    A[事件发布] --> B[获取所有监听器]
    B --> C[按Order排序]
    C --> D[遍历监听器]
    D --> E[执行监听器方法]
    E --> F{还有监听器?}
    F -->|是| D
    F -->|否| G[事件处理完成]

    %% 添加详细注释和说明
    A -.->|注释| A1[ApplicationEventPublisher<br/>发布事件对象]
    B -.->|注释| B1[从ApplicationEventMulticaster<br/>获取所有匹配的监听器]
    C -.->|注释| C1[根据@Order注解或Ordered接口<br/>按优先级排序，数值越小优先级越高]
    D -.->|注释| D1[按排序后的顺序<br/>逐个处理监听器]
    E -.->|注释| E1[调用监听器的<br/>onApplicationEvent方法]
    F -.->|注释| F1[检查是否还有<br/>其他监听器需要执行]
    G -.->|注释| G1[所有监听器执行完成<br/>事件处理流程结束]

    %% 添加执行顺序示例
    H[执行顺序示例] -.->|示例| H1[@Order(1) - 最高优先级<br/>@Order(100) - 中等优先级<br/>@Order(1000) - 最低优先级]
    
    %% 添加异常处理说明
    I[异常处理] -.->|说明| I1[监听器执行异常不会影响<br/>其他监听器的执行<br/>但会影响事件处理流程]

    %% 样式定义
    classDef processBox fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef decisionBox fill:#fff8e1,stroke:#f57c00,stroke-width:2px
    classDef endBox fill:#fce4ec,stroke:#c2185b,stroke-width:2px
    classDef commentBox fill:#f5f5f5,stroke:#616161,stroke-width:1px,stroke-dasharray: 5 5
    classDef exampleBox fill:#e3f2fd,stroke:#1976d2,stroke-width:2px

    class A,B,C,D,E processBox
    class F decisionBox
    class G endBox
    class A1,B1,C1,D1,E1,F1,G1,I1 commentBox
    class H,H1 exampleBox
```

## 9. 异步事件处理

### 9.1 异步事件处理配置

```java
@Configuration
@EnableAsync
public class AsyncEventConfig {
    
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return multicaster;
    }
}
```

### 9.2 异步事件监听器

```java
@Component
public class AsyncEventListener {
    
    @Async
    @EventListener
    public void handleAsyncEvent(ApplicationReadyEvent event) {
        // 异步处理事件
        System.out.println("Async event handling: " + Thread.currentThread().getName());
    }
}
```

### 9.3 异步处理流程图

```mermaid
flowchart TD
    A[事件发布] --> B[获取监听器]
    B --> C{是否异步?}
    C -->|是| D[提交到线程池]
    C -->|否| E[同步执行]
    D --> F[异步执行监听器]
    E --> G[同步执行监听器]
    F --> H[事件处理完成]
    G --> H

    %% 添加详细注释
    A -.->|注释| A1[ApplicationEventPublisher<br/>发布事件对象]
    B -.->|注释| B1[ApplicationEventMulticaster<br/>获取所有匹配的监听器]
    C -.->|注释| C1[检查监听器是否配置了<br/>@Async注解或异步执行器]
    D -.->|注释| D1[将监听器任务提交到<br/>TaskExecutor线程池]
    E -.->|注释| E1[在当前线程中<br/>同步执行监听器]
    F -.->|注释| F1[在独立线程中<br/>异步执行监听器方法]
    G -.->|注释| G1[在当前线程中<br/>同步执行监听器方法]
    H -.->|注释| H1[所有监听器执行完成<br/>事件处理流程结束]

    %% 添加异步配置说明
    I[异步配置] -.->|配置| I1[SimpleApplicationEventMulticaster<br/>setTaskExecutor(executor)<br/>启用异步事件处理]
    
    %% 添加线程池说明
    J[线程池配置] -.->|说明| J1[SimpleAsyncTaskExecutor<br/>ThreadPoolTaskExecutor<br/>自定义TaskExecutor]

    %% 添加异常处理说明
    K[异常处理] -.->|说明| K1[异步监听器异常不会影响<br/>同步监听器的执行<br/>但需要配置异常处理器]

    %% 样式定义
    classDef processBox fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef decisionBox fill:#fff8e1,stroke:#f57c00,stroke-width:2px
    classDef asyncBox fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
    classDef syncBox fill:#fce4ec,stroke:#c2185b,stroke-width:2px
    classDef endBox fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef commentBox fill:#f5f5f5,stroke:#616161,stroke-width:1px,stroke-dasharray: 5 5
    classDef configBox fill:#fff3e0,stroke:#e65100,stroke-width:2px

    class A,B processBox
    class C decisionBox
    class D,F asyncBox
    class E,G syncBox
    class H endBox
    class A1,B1,C1,D1,E1,F1,G1,H1,K1 commentBox
    class I,I1,J,J1 configBox
```

## 10. 自定义事件类型

### 10.1 定义自定义事件

```java
public class CustomStartupEvent extends ApplicationEvent {
    
    private final String message;
    private final Map<String, Object> data;
    
    public CustomStartupEvent(Object source, String message, Map<String, Object> data) {
        super(source);
        this.message = message;
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
}
```

### 10.2 发布和监听自定义事件

```java
@Component
public class CustomEventPublisher {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void publishCustomEvent() {
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", System.currentTimeMillis());
        data.put("version", "1.0.0");
        
        CustomStartupEvent event = new CustomStartupEvent(this, "Custom startup event", data);
        eventPublisher.publishEvent(event);
    }
}

@Component
public class CustomEventListener {
    
    @EventListener
    public void handleCustomEvent(CustomStartupEvent event) {
        System.out.println("Received custom event: " + event.getMessage());
        System.out.println("Event data: " + event.getData());
    }
}
```

## 11. 最佳实践和常见问题

### 11.1 事件监听器设计原则

1. **保持监听器方法简洁**: 只处理必要的逻辑
2. **异常处理**: 在监听器中添加适当的异常处理
3. **避免阻塞操作**: 对于耗时操作使用异步处理
4. **使用条件化监听**: 根据条件决定是否执行监听器
5. **合理使用执行顺序**: 通过@Order控制监听器执行顺序

### 11.2 实际应用示例

```java
@Component
public class BestPracticeEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(BestPracticeEventListener.class);
    
    @EventListener
    public void handleApplicationReady(ApplicationReadyEvent event) {
        try {
            performEssentialStartupTasks();
        } catch (Exception e) {
            logger.error("Error in startup event handler", e);
        }
    }
    
    @Async
    @EventListener
    public void handleNonCriticalEvent(ApplicationStartedEvent event) {
        performNonCriticalTasks();
    }
    
    @EventListener(condition = "#event.environment.getProperty('app.feature.enabled', 'false') == 'true'")
    public void handleFeatureEnabledEvent(ApplicationReadyEvent event) {
        enableFeature();
    }
    
    private void performEssentialStartupTasks() {
        // 关键启动任务
    }
    
    private void performNonCriticalTasks() {
        // 非关键任务
    }
    
    private void enableFeature() {
        // 启用特定功能
    }
}
```

### 11.3 常见问题解决

#### 问题1: 事件监听器未执行

- 确保监听器被正确注册为Spring Bean
- 检查事件类型是否匹配
- 验证条件化监听的条件是否正确

#### 问题2: 执行顺序问题

- 使用@Order注解控制执行顺序
- 实现Ordered接口
- 数值越小，优先级越高

#### 问题3: 异步事件处理异常

- 配置异常处理器
- 使用@EnableAsync启用异步支持
- 设置合适的线程池

## 12. 总结

SpringBoot的事件发布监听机制核心特点：

1. **完整的事件体系**: 覆盖启动流程的各个阶段
2. **灵活的事件监听**: 支持多种监听器实现方式
3. **强大的执行控制**: 支持顺序控制和条件化监听
4. **异步处理能力**: 支持异步事件处理
5. **自定义扩展**: 支持自定义事件类型和监听器

掌握这个机制对于SpringBoot应用的启动流程控制和扩展开发至关重要。
