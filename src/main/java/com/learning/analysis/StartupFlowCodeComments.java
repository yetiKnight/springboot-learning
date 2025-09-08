package com.learning.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * SpringBoot启动流程关键代码注释版本
 * 
 * 面试重点：通过代码注释详细说明启动流程的每个环节
 * 
 * @author 学习笔记
 */
@SpringBootApplication
public class StartupFlowCodeComments {

    public static void main(String[] args) {
        System.out.println("=== SpringBoot启动流程关键代码注释版本 ===");
        System.out.println();
        
        // ========================================
        // 阶段1：创建SpringApplication实例
        // ========================================
        System.out.println("【阶段1】创建SpringApplication实例");
        System.out.println("源码位置：SpringApplication构造器");
        System.out.println("关键步骤：");
        System.out.println("  1. 设置主类：this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources))");
        System.out.println("  2. 推断Web应用类型：this.webApplicationType = WebApplicationType.deduceFromClasspath()");
        System.out.println("  3. 加载ApplicationContextInitializer：getSpringFactoriesInstances(ApplicationContextInitializer.class)");
        System.out.println("  4. 加载ApplicationListener：getSpringFactoriesInstances(ApplicationListener.class)");
        System.out.println("  5. 推断主应用类：this.mainApplicationClass = deduceMainApplicationClass()");
        System.out.println();
        
        SpringApplication app = new SpringApplication(StartupFlowCodeComments.class);
        
        // ========================================
        // 阶段2：运行SpringApplication
        // ========================================
        System.out.println("【阶段2】运行SpringApplication.run()");
        System.out.println("源码位置：SpringApplication.run()方法");
        System.out.println("关键步骤：");
        System.out.println("  1. 创建BootstrapContext：createBootstrapContext()");
        System.out.println("  2. 准备环境：prepareEnvironment()");
        System.out.println("  3. 创建应用上下文：createApplicationContext()");
        System.out.println("  4. 刷新应用上下文：refreshContext(context) - 核心步骤");
        System.out.println("  5. 启动完成：listeners.started(context)");
        System.out.println();
        
        // 运行应用
        ConfigurableApplicationContext context = app.run(args);
        
        // ========================================
        // 阶段3：刷新应用上下文（核心步骤）
        // ========================================
        System.out.println("【阶段3】刷新应用上下文（核心步骤）");
        System.out.println("源码位置：AbstractApplicationContext.refresh()方法");
        System.out.println("关键步骤：");
        System.out.println("  1. 准备刷新：prepareRefresh()");
        System.out.println("  2. 获取BeanFactory：obtainFreshBeanFactory()");
        System.out.println("  3. 准备BeanFactory：prepareBeanFactory()");
        System.out.println("  4. 后处理BeanFactory：postProcessBeanFactory()");
        System.out.println("  5. 调用BeanFactoryPostProcessor：invokeBeanFactoryPostProcessors()");
        System.out.println("  6. 注册BeanPostProcessor：registerBeanPostProcessors()");
        System.out.println("  7. 初始化MessageSource：initMessageSource()");
        System.out.println("  8. 初始化ApplicationEventMulticaster：initApplicationEventMulticaster()");
        System.out.println("  9. 刷新特定上下文：onRefresh() - 启动内嵌服务器");
        System.out.println("  10. 注册监听器：registerListeners()");
        System.out.println("  11. 实例化所有单例Bean：finishBeanFactoryInitialization()");
        System.out.println("  12. 完成刷新：finishRefresh()");
        System.out.println();
        
        // ========================================
        // 阶段4：启动内嵌Web服务器
        // ========================================
        System.out.println("【阶段4】启动内嵌Web服务器");
        System.out.println("源码位置：ServletWebServerApplicationContext.onRefresh()");
        System.out.println("关键步骤：");
        System.out.println("  1. 创建Web服务器：createWebServer()");
        System.out.println("  2. 获取ServletWebServerFactory：getWebServerFactory()");
        System.out.println("  3. 创建Web服务器实例：factory.getWebServer()");
        System.out.println("  4. 启动Tomcat服务器：tomcat.start()");
        System.out.println("  5. 等待请求：tomcat.getServer().await()");
        System.out.println();
        
        // ========================================
        // 阶段5：Bean生命周期
        // ========================================
        System.out.println("【阶段5】Bean生命周期");
        System.out.println("源码位置：AbstractBeanFactory.doGetBean()");
        System.out.println("关键步骤：");
        System.out.println("  1. 构造函数调用：new Bean()");
        System.out.println("  2. BeanNameAware.setBeanName()：设置Bean名称");
        System.out.println("  3. BeanPostProcessor.postProcessBeforeInitialization()：初始化前处理");
        System.out.println("  4. @PostConstruct方法调用：初始化方法");
        System.out.println("  5. InitializingBean.afterPropertiesSet()：属性设置后处理");
        System.out.println("  6. BeanPostProcessor.postProcessAfterInitialization()：初始化后处理");
        System.out.println("  7. Bean使用阶段：执行业务逻辑");
        System.out.println("  8. @PreDestroy方法调用：销毁前处理");
        System.out.println("  9. DisposableBean.destroy()：销毁处理");
        System.out.println();
        
        // ========================================
        // 阶段6：自动配置原理
        // ========================================
        System.out.println("【阶段6】自动配置原理");
        System.out.println("源码位置：AutoConfigurationImportSelector.selectImports()");
        System.out.println("关键步骤：");
        System.out.println("  1. 加载spring.factories文件：SpringFactoriesLoader.loadFactoryNames()");
        System.out.println("  2. 过滤自动配置类：filter()");
        System.out.println("  3. 条件注解判断：@ConditionalOnClass, @ConditionalOnProperty等");
        System.out.println("  4. 创建配置类实例：createSpringFactoriesInstances()");
        System.out.println("  5. 注册Bean定义：registerBeanDefinitions()");
        System.out.println();
        
        // ========================================
        // 启动结果分析
        // ========================================
        System.out.println("【启动结果分析】");
        System.out.println("📊 Bean数量: " + context.getBeanDefinitionCount());
        System.out.println("🏗️ 应用类型: " + context.getClass().getSimpleName());
        System.out.println("🌍 环境类型: " + context.getEnvironment().getClass().getSimpleName());
        System.out.println("📋 活跃Profile: " + String.join(",", context.getEnvironment().getActiveProfiles()));
        System.out.println();
        
        // 关闭应用
        System.out.println("🔄 关闭应用...");
        context.close();
        System.out.println("✅ 应用已关闭");
    }
}
