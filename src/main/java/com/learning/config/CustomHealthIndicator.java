package com.learning.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 自定义健康检查器
 * 
 * 面试重点知识点：
 * 1. HealthIndicator接口的实现
 * 2. 自定义健康检查逻辑
 * 3. 健康状态的分类和判断
 * 
 * @author 学习笔记
 */
@Component("customHealthIndicator")
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // 自定义健康检查逻辑
        try {
            // 检查应用状态
            if (isApplicationHealthy()) {
                return Health.up()
                    .withDetail("custom", "Application is healthy")
                    .withDetail("timestamp", System.currentTimeMillis())
                    .build();
            } else {
                return Health.down()
                    .withDetail("custom", "Application is unhealthy")
                    .withDetail("timestamp", System.currentTimeMillis())
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("custom", "Health check failed: " + e.getMessage())
                .withDetail("timestamp", System.currentTimeMillis())
                .build();
        }
    }

    /**
     * 检查应用是否健康
     * 面试重点：健康检查的业务逻辑
     */
    private boolean isApplicationHealthy() {
        // 这里可以添加具体的健康检查逻辑
        // 例如：检查数据库连接、外部服务状态等
        
        // 模拟健康检查
        return true; // 假设应用是健康的
    }
}
