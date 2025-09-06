package com.learning.controller;

import com.learning.entity.User;
import com.learning.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 用户控制器
 * 
 * 面试重点知识点：
 * 1. @RestController注解的作用和原理
 * 2. RESTful API设计原则
 * 3. HTTP状态码的使用
 * 4. 参数验证和异常处理
 * 5. 分页查询的实现
 * 6. 响应体的设计
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 获取所有用户
     * 面试重点：分页查询、RESTful设计
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("获取所有用户");
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * 分页查询用户
     * 面试重点：Pageable参数、分页响应
     */
    @GetMapping("/page")
    public ResponseEntity<Page<User>> getUsersWithPagination(Pageable pageable) {
        log.info("分页查询用户: {}", pageable);
        // 这里需要在Service中实现分页查询
        return ResponseEntity.ok(Page.empty());
    }

    /**
     * 根据ID获取用户
     * 面试重点：路径参数、Optional处理
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("根据ID获取用户: {}", id);
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据用户名获取用户
     * 面试重点：查询参数、参数验证
     */
    @GetMapping("/search")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        log.info("根据用户名获取用户: {}", username);
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建用户
     * 面试重点：@RequestBody、参数验证、HTTP状态码
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("创建用户: {}", user);
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * 更新用户
     * 面试重点：PUT方法、完整更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("更新用户: {}, 数据: {}", id, user);
        user.setId(id);
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 部分更新用户
     * 面试重点：PATCH方法、部分更新
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("部分更新用户: {}, 数据: {}", id, user);
        // 这里需要实现部分更新逻辑
        return ResponseEntity.ok(user);
    }

    /**
     * 删除用户
     * 面试重点：DELETE方法、无响应体
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户: {}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
