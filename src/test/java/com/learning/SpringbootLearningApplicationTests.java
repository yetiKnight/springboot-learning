package com.learning;

import com.learning.entity.User;
import com.learning.repository.UserRepository;
import com.learning.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SpringBoot应用测试类
 * 
 * 面试重点知识点：
 * 1. SpringBoot测试框架的使用
 * 2. 集成测试的编写
 * 3. 测试配置和Profile
 * 4. 断言的使用
 * 5. 测试数据的管理
 * 
 * @author 学习笔记
 */
@SpringBootTest
@ActiveProfiles("test")
class SpringbootLearningApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 测试应用上下文加载
     * 面试重点：基础集成测试
     */
    @Test
    void contextLoads() {
        // 验证应用上下文能够正常加载
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    /**
     * 测试用户创建和查询
     * 面试重点：业务逻辑测试
     */
    @Test
    void testUserCreationAndRetrieval() {
        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        // 保存用户
        User savedUser = userService.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");

        // 查询用户
        var foundUser = userService.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    /**
     * 测试用户查询
     * 面试重点：查询方法测试
     */
    @Test
    void testUserQuery() {
        // 创建测试用户
        User user = new User();
        user.setUsername("queryuser");
        user.setPassword("password123");
        user.setEmail("query@example.com");
        user.setFirstName("Query");
        user.setLastName("User");

        User savedUser = userService.save(user);

        // 测试根据用户名查询
        var foundUser = userService.findByUsername("queryuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("queryuser");

        // 测试查询所有用户
        var allUsers = userService.findAll();
        assertThat(allUsers).isNotEmpty();
    }

    /**
     * 测试用户更新
     * 面试重点：更新操作测试
     */
    @Test
    void testUserUpdate() {
        // 创建测试用户
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("password123");
        user.setEmail("update@example.com");
        user.setFirstName("Update");
        user.setLastName("User");

        User savedUser = userService.save(user);

        // 更新用户信息
        savedUser.setFirstName("Updated");
        savedUser.setLastName("User");
        User updatedUser = userService.update(savedUser);

        assertThat(updatedUser.getFirstName()).isEqualTo("Updated");
        assertThat(updatedUser.getLastName()).isEqualTo("User");
    }

    /**
     * 测试用户删除
     * 面试重点：删除操作测试
     */
    @Test
    void testUserDeletion() {
        // 创建测试用户
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("password123");
        user.setEmail("delete@example.com");
        user.setFirstName("Delete");
        user.setLastName("User");

        User savedUser = userService.save(user);
        Long userId = savedUser.getId();

        // 删除用户
        userService.deleteById(userId);

        // 验证用户已被删除
        var foundUser = userService.findById(userId);
        assertThat(foundUser).isEmpty();
    }
}
