package com.learning.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义自动配置类
 * 
 * 面试重点知识点：
 * 1. 自动配置类的编写规范
 * 2. 条件注解的使用场景和原理
 * 3. @ConditionalOnClass、@ConditionalOnMissingBean等条件注解
 * 4. 如何让SpringBoot自动加载我们的配置类
 * 
 * @author 学习笔记
 */
@Configuration
@ConditionalOnClass(name = "com.learning.service.CustomService")
@ConditionalOnProperty(prefix = "app.custom", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CustomAutoConfiguration {

    /**
     * 自定义服务Bean
     * 面试重点：条件注解的组合使用
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomService customService() {
        return new CustomService();
    }

    /**
     * 自定义服务实现类
     */
    public static class CustomService {
        public String getMessage() {
            return "Hello from Custom Auto Configuration!";
        }
    }
}
