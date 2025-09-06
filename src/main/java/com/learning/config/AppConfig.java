package com.learning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 应用配置类
 * 
 * 面试重点知识点：
 * 1. @Configuration 注解的作用和原理
 * 2. @ConfigurationProperties 外部化配置
 * 3. @Bean 注解的Bean定义和生命周期
 * 4. 条件注解的使用场景
 * 
 * @author 学习笔记
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {

    private Jwt jwt = new Jwt();
    private Cache cache = new Cache();
    private Async async = new Async();

    @Data
    public static class Jwt {
        private String secret = "mySecretKey";
        private Long expiration = 86400000L; // 24小时
    }

    @Data
    public static class Cache {
        private Integer defaultTtl = 3600; // 1小时
    }

    @Data
    public static class Async {
        private Integer corePoolSize = 5;
        private Integer maxPoolSize = 20;
        private Integer queueCapacity = 100;
    }

    /**
     * 自定义线程池配置
     * 面试重点：线程池参数调优、拒绝策略
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(async.getCorePoolSize());
        executor.setMaxPoolSize(async.getMaxPoolSize());
        executor.setQueueCapacity(async.getQueueCapacity());
        executor.setThreadNamePrefix("async-");
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
