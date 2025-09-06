package com.learning.controller;

import com.learning.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存控制器
 * 
 * 面试重点知识点：
 * 1. 缓存API的设计
 * 2. 缓存操作的监控
 * 3. 缓存性能测试
 * 4. 缓存问题的诊断
 * 5. 缓存策略的验证
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final CacheService cacheService;

    /**
     * 基础缓存操作
     * 面试重点：缓存API设计
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        log.info("查询用户缓存，ID：{}", id);
        Map<String, Object> user = cacheService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 条件缓存操作
     * 面试重点：条件缓存的使用
     */
    @GetMapping("/user/{id}/conditional")
    public ResponseEntity<Map<String, Object>> getUserByIdWithCondition(@PathVariable Long id) {
        log.info("条件查询用户缓存，ID：{}", id);
        Map<String, Object> user = cacheService.getUserByIdWithCondition(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 缓存更新操作
     * 面试重点：缓存更新API
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, 
                                                          @RequestBody Map<String, Object> userData) {
        log.info("更新用户缓存，ID：{}", id);
        Map<String, Object> updatedUser = cacheService.updateUser(id, userData);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 缓存删除操作
     * 面试重点：缓存删除API
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户缓存，ID：{}", id);
        cacheService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 清除所有缓存
     * 面试重点：批量缓存操作
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllCache() {
        log.info("清除所有用户缓存");
        cacheService.clearAllUserCache();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "所有用户缓存已清除");
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 组合缓存操作
     * 面试重点：复杂缓存操作
     */
    @GetMapping("/user/{id}/caching")
    public ResponseEntity<Map<String, Object>> getUserWithCaching(@PathVariable Long id) {
        log.info("组合缓存操作，ID：{}", id);
        Map<String, Object> user = cacheService.getUserWithCaching(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 多级缓存操作
     * 面试重点：多级缓存测试
     */
    @GetMapping("/user/{id}/multi-level")
    public ResponseEntity<Map<String, Object>> getUserWithMultiLevelCache(@PathVariable Long id) {
        log.info("多级缓存查询，ID：{}", id);
        Map<String, Object> user = cacheService.getUserWithMultiLevelCache(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 缓存穿透防护测试
     * 面试重点：缓存穿透测试
     */
    @GetMapping("/user/{id}/penetration-protection")
    public ResponseEntity<Map<String, Object>> getUserWithPenetrationProtection(@PathVariable Long id) {
        log.info("缓存穿透防护测试，ID：{}", id);
        Map<String, Object> user = cacheService.getUserWithPenetrationProtection(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 缓存雪崩防护测试
     * 面试重点：缓存雪崩测试
     */
    @GetMapping("/user/{id}/avalanche-protection")
    public ResponseEntity<Map<String, Object>> getUserWithAvalancheProtection(@PathVariable Long id) {
        log.info("缓存雪崩防护测试，ID：{}", id);
        Map<String, Object> user = cacheService.getUserWithAvalancheProtection(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 缓存击穿防护测试
     * 面试重点：缓存击穿测试
     */
    @GetMapping("/user/{id}/breakdown-protection")
    public ResponseEntity<Map<String, Object>> getUserWithBreakdownProtection(@PathVariable Long id) {
        log.info("缓存击穿防护测试，ID：{}", id);
        Map<String, Object> user = cacheService.getUserWithBreakdownProtection(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 缓存性能测试
     * 面试重点：缓存性能测试
     */
    @GetMapping("/performance-test")
    public ResponseEntity<Map<String, Object>> performanceTest(@RequestParam(defaultValue = "100") int count) {
        log.info("缓存性能测试，测试次数：{}", count);
        
        long startTime = System.currentTimeMillis();
        
        // 测试缓存命中
        for (int i = 1; i <= count; i++) {
            cacheService.getUserById((long) i);
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        Map<String, Object> result = new HashMap<>();
        result.put("testCount", count);
        result.put("executionTime", executionTime + "ms");
        result.put("averageTime", (double) executionTime / count + "ms");
        result.put("throughput", count * 1000.0 / executionTime + " requests/second");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 缓存统计信息
     * 面试重点：缓存监控
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        log.info("获取缓存统计信息");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("timestamp", System.currentTimeMillis());
        statistics.put("cacheNames", new String[]{"userCache", "orderCache"});
        statistics.put("totalCaches", 2);
        statistics.put("status", "healthy");
        
        // 模拟缓存统计信息
        Map<String, Object> userCacheStats = new HashMap<>();
        userCacheStats.put("hitCount", 1250);
        userCacheStats.put("missCount", 150);
        userCacheStats.put("hitRate", "89.3%");
        userCacheStats.put("size", 500);
        userCacheStats.put("evictionCount", 25);
        
        statistics.put("userCache", userCacheStats);
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * 缓存预热
     * 面试重点：缓存预热策略
     */
    @PostMapping("/warmup")
    public ResponseEntity<Map<String, Object>> warmupCache(@RequestParam(defaultValue = "100") int count) {
        log.info("缓存预热，预热数量：{}", count);
        
        long startTime = System.currentTimeMillis();
        
        // 预热缓存
        for (int i = 1; i <= count; i++) {
            cacheService.getUserById((long) i);
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        Map<String, Object> result = new HashMap<>();
        result.put("warmupCount", count);
        result.put("executionTime", executionTime + "ms");
        result.put("message", "缓存预热完成");
        
        return ResponseEntity.ok(result);
    }
}
