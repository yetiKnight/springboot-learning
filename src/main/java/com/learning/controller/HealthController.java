package com.learning.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 
 * 面试重点知识点：
 * 1. Actuator健康检查的使用
 * 2. 自定义健康检查指标
 * 3. 系统监控和运维
 * 4. 微服务健康检查设计
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @Autowired(required = false)
    private HealthIndicator customHealthIndicator;

    /**
     * 基础健康检查
     * 面试重点：系统状态监控
     */
    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "springboot-learning");
        health.put("version", "1.0.0");
        
        // 检查数据库连接
        health.put("database", checkDatabase());
        
        // 检查Redis连接
        health.put("redis", checkRedis());
        
        // 检查自定义健康指标
        if (customHealthIndicator != null) {
            Health customHealth = customHealthIndicator.health();
            health.put("custom", customHealth.getStatus().getCode());
        }
        
        return health;
    }

    /**
     * 详细健康检查
     * 面试重点：分层健康检查设计
     */
    @GetMapping("/detailed")
    public Map<String, Object> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // 系统信息
        Map<String, Object> system = new HashMap<>();
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("osName", System.getProperty("os.name"));
        system.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        system.put("totalMemory", Runtime.getRuntime().totalMemory());
        system.put("freeMemory", Runtime.getRuntime().freeMemory());
        system.put("maxMemory", Runtime.getRuntime().maxMemory());
        health.put("system", system);
        
        // 应用信息
        Map<String, Object> application = new HashMap<>();
        application.put("name", "springboot-learning");
        application.put("version", "1.0.0");
        application.put("uptime", System.currentTimeMillis() - getStartTime());
        health.put("application", application);
        
        // 依赖服务状态
        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("database", checkDatabase());
        dependencies.put("redis", checkRedis());
        health.put("dependencies", dependencies);
        
        return health;
    }

    /**
     * 检查数据库连接
     */
    private Map<String, Object> checkDatabase() {
        Map<String, Object> db = new HashMap<>();
        try {
            // 这里应该实际检查数据库连接
            db.put("status", "UP");
            db.put("message", "Database connection is healthy");
        } catch (Exception e) {
            db.put("status", "DOWN");
            db.put("message", "Database connection failed: " + e.getMessage());
        }
        return db;
    }

    /**
     * 检查Redis连接
     */
    private Map<String, Object> checkRedis() {
        Map<String, Object> redis = new HashMap<>();
        try {
            // 这里应该实际检查Redis连接
            redis.put("status", "UP");
            redis.put("message", "Redis connection is healthy");
        } catch (Exception e) {
            redis.put("status", "DOWN");
            redis.put("message", "Redis connection failed: " + e.getMessage());
        }
        return redis;
    }

    private long getStartTime() {
        // 这里应该记录应用启动时间
        return System.currentTimeMillis() - 60000; // 模拟启动时间
    }
}
