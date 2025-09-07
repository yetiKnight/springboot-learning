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
 * SpringBoot启动流程演示
 * 
 * 面试重点：通过实际代码演示启动流程的各个阶段
 * 
 * @author 学习笔记
 */
@SpringBootApplication
public class StartupFlowDemo {

    public static void main(String[] args) {
        System.out.println("=== SpringBoot启动流程演示 ===");
        
        // 创建SpringApplication实例
        SpringApplication app = new SpringApplication(StartupFlowDemo.class);
        
        // 添加自定义初始化器
        app.addInitializers(new CustomApplicationContextInitializer());
        
        // 添加自定义监听器
        app.addListeners(new CustomApplicationListener());
        
        // 运行应用
        ConfigurableApplicationContext context = app.run(args);
        
        System.out.println("=== 启动完成 ===");
        System.out.println("Bean数量: " + context.getBeanDefinitionCount());
        System.out.println("应用类型: " + context.getClass().getSimpleName());
        
        // 关闭应用
        context.close();
    }
    
    /**
     * 自定义ApplicationContextInitializer
     * 面试重点：理解初始化器的作用时机
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            System.out.println("=== 阶段1：ApplicationContextInitializer.initialize() ===");
            System.out.println("上下文类型: " + applicationContext.getClass().getSimpleName());
            System.out.println("环境信息: " + applicationContext.getEnvironment().getClass().getSimpleName());
            System.out.println("活跃Profile: " + String.join(",", applicationContext.getEnvironment().getActiveProfiles()));
        }
    }
    
    /**
     * 自定义ApplicationListener
     * 面试重点：理解事件监听器的作用
     */
    public static class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
        
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println("=== 阶段2：ApplicationListener.onApplicationEvent() ===");
            System.out.println("事件类型: " + event.getClass().getSimpleName());
            System.out.println("事件源: " + event.getSource().getClass().getSimpleName());
            System.out.println("时间戳: " + event.getTimestamp());
        }
    }
}
