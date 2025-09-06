package com.learning.service;

import com.learning.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 缓存服务类
 * 
 * 面试重点知识点：
 * 1. Spring Cache抽象的使用
 * 2. 缓存注解的详细用法
 * 3. 缓存策略的设计
 * 4. 缓存穿透、雪崩、击穿的解决方案
 * 5. 缓存性能优化
 * 
 * @author 学习笔记
 */
@Service
@Slf4j
public class CacheService {

    /**
     * 基础缓存操作 - @Cacheable
     * 面试重点：@Cacheable注解的使用
     */
    @Cacheable(value = "userCache", key = "#id")
    @LogExecutionTime
    public Map<String, Object> getUserById(Long id) {
        log.info("查询用户信息，ID：{}", id);
        
        // 模拟数据库查询
        simulateDatabaseQuery(100);
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        
        return user;
    }

    /**
     * 条件缓存 - 根据条件决定是否缓存
     * 面试重点：condition和unless参数的使用
     */
    @Cacheable(value = "userCache", key = "#id", condition = "#id > 0", unless = "#result == null")
    @LogExecutionTime
    public Map<String, Object> getUserByIdWithCondition(Long id) {
        log.info("条件查询用户信息，ID：{}", id);
        
        if (id <= 0) {
            return null;
        }
        
        simulateDatabaseQuery(150);
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        
        return user;
    }

    /**
     * 缓存更新 - @CachePut
     * 面试重点：@CachePut注解的使用
     */
    @CachePut(value = "userCache", key = "#id")
    @LogExecutionTime
    public Map<String, Object> updateUser(Long id, Map<String, Object> userData) {
        log.info("更新用户信息，ID：{}", id);
        
        // 模拟数据库更新
        simulateDatabaseQuery(200);
        
        Map<String, Object> updatedUser = new HashMap<>(userData);
        updatedUser.put("id", id);
        updatedUser.put("updatedAt", LocalDateTime.now());
        
        return updatedUser;
    }

    /**
     * 缓存清除 - @CacheEvict
     * 面试重点：@CacheEvict注解的使用
     */
    @CacheEvict(value = "userCache", key = "#id")
    @LogExecutionTime
    public void deleteUser(Long id) {
        log.info("删除用户，ID：{}", id);
        
        // 模拟数据库删除
        simulateDatabaseQuery(50);
    }

    /**
     * 批量缓存清除
     * 面试重点：allEntries参数的使用
     */
    @CacheEvict(value = "userCache", allEntries = true)
    @LogExecutionTime
    public void clearAllUserCache() {
        log.info("清除所有用户缓存");
    }

    /**
     * 组合缓存操作 - @Caching
     * 面试重点：@Caching注解的使用
     */
    @Caching(
        cacheable = @Cacheable(value = "userCache", key = "#id"),
        evict = @CacheEvict(value = "userCache", key = "#id", condition = "#id > 100")
    )
    @LogExecutionTime
    public Map<String, Object> getUserWithCaching(Long id) {
        log.info("组合缓存操作，ID：{}", id);
        
        simulateDatabaseQuery(120);
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        
        return user;
    }

    /**
     * 多级缓存操作
     * 面试重点：多级缓存的设计
     */
    @Cacheable(value = "userCache", key = "#id")
    @LogExecutionTime
    public Map<String, Object> getUserWithMultiLevelCache(Long id) {
        log.info("多级缓存查询，ID：{}", id);
        
        // 第一级：本地缓存
        Map<String, Object> localCache = getFromLocalCache(id);
        if (localCache != null) {
            log.info("从本地缓存获取数据");
            return localCache;
        }
        
        // 第二级：分布式缓存
        Map<String, Object> distributedCache = getFromDistributedCache(id);
        if (distributedCache != null) {
            log.info("从分布式缓存获取数据");
            // 更新本地缓存
            putToLocalCache(id, distributedCache);
            return distributedCache;
        }
        
        // 第三级：数据库
        log.info("从数据库获取数据");
        Map<String, Object> user = getFromDatabase(id);
        
        // 更新缓存
        putToLocalCache(id, user);
        putToDistributedCache(id, user);
        
        return user;
    }

    /**
     * 缓存穿透防护
     * 面试重点：缓存穿透的解决方案
     */
    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    @LogExecutionTime
    public Map<String, Object> getUserWithPenetrationProtection(Long id) {
        log.info("缓存穿透防护查询，ID：{}", id);
        
        // 模拟数据库查询
        simulateDatabaseQuery(100);
        
        // 模拟数据不存在的情况
        if (id > 1000) {
            log.info("用户不存在，ID：{}", id);
            return null;
        }
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        
        return user;
    }

    /**
     * 缓存雪崩防护
     * 面试重点：缓存雪崩的解决方案
     */
    @Cacheable(value = "userCache", key = "#id")
    @LogExecutionTime
    public Map<String, Object> getUserWithAvalancheProtection(Long id) {
        log.info("缓存雪崩防护查询，ID：{}", id);
        
        // 添加随机延迟，避免缓存同时过期
        int randomDelay = ThreadLocalRandom.current().nextInt(100, 500);
        simulateDatabaseQuery(randomDelay);
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        user.put("expireTime", LocalDateTime.now().plusMinutes(5 + ThreadLocalRandom.current().nextInt(0, 5)));
        
        return user;
    }

    /**
     * 缓存击穿防护
     * 面试重点：缓存击穿的解决方案
     */
    @Cacheable(value = "userCache", key = "#id")
    @LogExecutionTime
    public Map<String, Object> getUserWithBreakdownProtection(Long id) {
        log.info("缓存击穿防护查询，ID：{}", id);
        
        // 模拟热点数据查询
        simulateDatabaseQuery(200);
        
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        user.put("isHotData", true);
        
        return user;
    }

    /**
     * 模拟数据库查询
     */
    private void simulateDatabaseQuery(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 模拟本地缓存
     */
    private Map<String, Object> getFromLocalCache(Long id) {
        // 模拟本地缓存查询
        return null;
    }

    /**
     * 模拟分布式缓存
     */
    private Map<String, Object> getFromDistributedCache(Long id) {
        // 模拟分布式缓存查询
        return null;
    }

    /**
     * 模拟数据库查询
     */
    private Map<String, Object> getFromDatabase(Long id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("createdAt", LocalDateTime.now());
        return user;
    }

    /**
     * 更新本地缓存
     */
    private void putToLocalCache(Long id, Map<String, Object> user) {
        // 模拟本地缓存更新
    }

    /**
     * 更新分布式缓存
     */
    private void putToDistributedCache(Long id, Map<String, Object> user) {
        // 模拟分布式缓存更新
    }
}
