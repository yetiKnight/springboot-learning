package com.learning.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * SpringBoot启动流程可视化演示
 * 
 * 面试重点：按照实际执行顺序展示启动流程的关键环节
 * 
 * @author 学习笔记
 */
@SpringBootApplication
public class StartupFlowVisualization {

    public static void main(String[] args) {
        System.out.println("🚀 === SpringBoot启动流程可视化演示 ===");
        System.out.println();
        
        // 阶段1：创建SpringApplication实例
        System.out.println("📝 阶段1：创建SpringApplication实例");
        SpringApplication app = new SpringApplication(StartupFlowVisualization.class);
        System.out.println("✅ SpringApplication实例创建完成");
        System.out.println();
        
        // 阶段2：运行SpringApplication
        System.out.println("📝 阶段2：运行SpringApplication.run()");
        System.out.println("  ├─ 2.1 创建BootstrapContext");
        System.out.println("  ├─ 2.2 准备环境");
        System.out.println("  ├─ 2.3 创建应用上下文");
        System.out.println("  ├─ 2.4 刷新应用上下文（核心）");
        System.out.println("  └─ 2.5 启动完成");
        System.out.println();
        
        // 添加自定义初始化器
        app.addInitializers(new CustomApplicationContextInitializer());
        
        // 添加自定义监听器
        app.addListeners(new CustomApplicationListener());
        
        // 运行应用
        ConfigurableApplicationContext context = app.run(args);
        
        System.out.println("🎉 === 启动完成 ===");
        System.out.println("📊 Bean数量: " + context.getBeanDefinitionCount());
        System.out.println("🏗️ 应用类型: " + context.getClass().getSimpleName());
        System.out.println();
        
        // 关闭应用
        System.out.println("🔄 关闭应用...");
        context.close();
        System.out.println("✅ 应用已关闭");
    }
    
    /**
     * 自定义ApplicationContextInitializer
     * 执行时机：在ApplicationContext刷新之前
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            System.out.println("🔧 === ApplicationContextInitializer.initialize() ===");
            System.out.println("  📍 执行时机：ApplicationContext刷新之前");
            System.out.println("  🏗️ 上下文类型: " + applicationContext.getClass().getSimpleName());
            System.out.println("  🌍 环境信息: " + applicationContext.getEnvironment().getClass().getSimpleName());
            System.out.println("  📋 活跃Profile: " + String.join(",", applicationContext.getEnvironment().getActiveProfiles()));
            System.out.println();
        }
    }
    
    /**
     * 自定义ApplicationListener
     * 执行时机：监听ContextRefreshedEvent事件
     */
    public static class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
        
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println("👂 === ApplicationListener.onApplicationEvent() ===");
            System.out.println("  📍 执行时机：ApplicationContext刷新完成");
            System.out.println("  📅 事件类型: " + event.getClass().getSimpleName());
            System.out.println("  🎯 事件源: " + event.getSource().getClass().getSimpleName());
            System.out.println("  ⏰ 时间戳: " + event.getTimestamp());
            System.out.println();
        }
    }
}
