package com.learning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.annotation.LogExecutionTime;
import com.learning.entity.User;
import com.learning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务类
 * 
 * 面试重点知识点：
 * 1. @Service注解的作用和原理
 * 2. 依赖注入的方式：构造器注入、字段注入、setter注入
 * 3. @Transactional事务管理
 * 4. @Cacheable缓存注解
 * 5. MyBatis Plus条件构造器
 * 
 * @author 学习笔记
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 根据ID查询用户
     * 面试重点：缓存注解的使用和原理
     */
    @Cacheable(value = "users", key = "#id")
    @LogExecutionTime(logArgs = true, logResult = true)
    public User findById(Long id) {
        log.info("查询用户: {}", id);
        return userRepository.selectById(id);
    }

    /**
     * 查询所有用户
     * 面试重点：事务只读优化
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<User> findAll() {
        log.info("查询所有用户");
        return userRepository.selectList(null);
    }

    /**
     * 分页查询用户
     * 面试重点：MyBatis Plus分页插件
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public IPage<User> findPage(Page<User> page) {
        log.info("分页查询用户: {}", page);
        return userRepository.selectPage(page, null);
    }

    /**
     * 保存用户
     * 面试重点：事务回滚规则、缓存清除
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "users", allEntries = true)
    @LogExecutionTime(logArgs = true, logResult = true)
    public User save(User user) {
        log.info("保存用户: {}", user);
        userRepository.insert(user);
        return user;
    }

    /**
     * 更新用户
     * 面试重点：事务传播机制
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "users", key = "#user.id")
    @LogExecutionTime(logArgs = true, logResult = true)
    public User update(User user) {
        log.info("更新用户: {}", user);
        userRepository.updateById(user);
        return user;
    }

    /**
     * 删除用户（逻辑删除）
     * 面试重点：事务管理、缓存清除、逻辑删除
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "users", key = "#id")
    @LogExecutionTime(logArgs = true)
    public void deleteById(Long id) {
        log.info("删除用户: {}", id);
        userRepository.deleteById(id);
    }

    /**
     * 根据用户名查询用户
     * 面试重点：自定义查询方法
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public User findByUsername(String username) {
        log.info("根据用户名查询用户: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * 根据条件查询用户
     * 面试重点：MyBatis Plus条件构造器
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<User> findByCondition(User user) {
        log.info("根据条件查询用户: {}", user);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (user.getUsername() != null) {
            queryWrapper.like("username", user.getUsername());
        }
        if (user.getEmail() != null) {
            queryWrapper.like("email", user.getEmail());
        }
        if (user.getStatus() != null) {
            queryWrapper.eq("status", user.getStatus());
        }
        
        return userRepository.selectList(queryWrapper);
    }
}
