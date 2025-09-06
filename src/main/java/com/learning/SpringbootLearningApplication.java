package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot 主启动类
 * 
 * 面试重点知识点：
 * 1. @SpringBootApplication 注解的组成和作用
 * 2. 自动配置原理和条件注解
 * 3. 启动流程和Bean的创建过程
 * 4. 内嵌Tomcat服务器原理
 * 
 * @author 学习笔记
 * @since 1.0.0
 */
@SpringBootApplication
@EnableCaching  // 启用缓存支持
@EnableAsync    // 启用异步方法支持
@EnableScheduling // 启用定时任务支持
public class SpringbootLearningApplication {

    public static void main(String[] args) {
        // 面试重点：SpringApplication.run() 方法做了什么？
        // 1. 创建SpringApplication实例
        // 2. 加载应用上下文
        // 3. 刷新上下文，完成Bean的创建和依赖注入
        // 4. 启动内嵌Web服务器
        SpringApplication.run(SpringbootLearningApplication.class, args);
    }
}
