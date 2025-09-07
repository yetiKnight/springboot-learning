package com.learning.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置原理演示
 * 
 * 面试重点：通过代码演示自动配置的工作原理
 * 
 * @author 学习笔记
 */
public class AutoConfigurationDemo {

    public static void main(String[] args) {
        System.out.println("=== 自动配置原理演示 ===");
        
        SpringApplication app = new SpringApplication(AutoConfigurationDemo.class);
        
        // 启用调试模式
        app.setAdditionalProfiles("debug");
        
        ConfigurableApplicationContext context = app.run(args);
        
        // 分析自动配置结果
        analyzeAutoConfiguration(context);
    }
    
    /**
     * 分析自动配置结果
     */
    private static void analyzeAutoConfiguration(ConfigurableApplicationContext context) {
        System.out.println("\n=== 自动配置分析 ===");
        
        // 检查自定义配置是否生效
        if (context.containsBean("customService")) {
            System.out.println("✅ 自定义服务已自动配置");
            Object service = context.getBean("customService");
            System.out.println("服务类型: " + service.getClass().getSimpleName());
        } else {
            System.out.println("❌ 自定义服务未自动配置");
        }
        
        // 检查条件配置是否生效
        if (context.containsBean("conditionalService")) {
            System.out.println("✅ 条件服务已自动配置");
        } else {
            System.out.println("❌ 条件服务未自动配置");
        }
    }
    
    /**
     * 自定义自动配置类
     * 面试重点：理解自动配置类的编写
     */
    @Configuration
    @ConditionalOnClass(name = "com.learning.analysis.AutoConfigurationDemo")
    @ConditionalOnProperty(prefix = "app.auto", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class CustomAutoConfiguration {
        
        @Bean
        @ConditionalOnMissingBean
        public CustomService customService() {
            System.out.println("=== 自动配置：创建CustomService ===");
            return new CustomService();
        }
    }
    
    /**
     * 条件配置类
     * 面试重点：理解条件注解的使用
     */
    @Configuration
    @ConditionalOnClass(name = "java.lang.String")
    @ConditionalOnProperty(prefix = "app.conditional", name = "enabled", havingValue = "true")
    public static class ConditionalConfiguration {
        
        @Bean
        public ConditionalService conditionalService() {
            System.out.println("=== 条件配置：创建ConditionalService ===");
            return new ConditionalService();
        }
    }
    
    /**
     * 自定义服务
     */
    public static class CustomService {
        public String getMessage() {
            return "Hello from Custom Auto Configuration!";
        }
    }
    
    /**
     * 条件服务
     */
    public static class ConditionalService {
        public String getMessage() {
            return "Hello from Conditional Configuration!";
        }
    }
}
