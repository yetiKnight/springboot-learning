package com.learning.repository;

import com.learning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 * 
 * 面试重点知识点：
 * 1. JpaRepository接口的作用和继承关系
 * 2. 方法名查询的规则和实现原理
 * 3. @Query注解的使用：JPQL和原生SQL
 * 4. 参数绑定的方式：@Param注解
 * 5. 分页和排序的实现
 * 
 * @author 学习笔记
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     * 面试重点：方法名查询的命名规则
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据状态查询用户列表
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * 根据用户名模糊查询
     * 面试重点：Like查询的写法
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * 根据邮箱模糊查询
     */
    List<User> findByEmailContainingIgnoreCase(String email);

    /**
     * 自定义JPQL查询
     * 面试重点：JPQL语法、参数绑定
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.createdAt >= :startDate")
    List<User> findActiveUsersAfterDate(@Param("status") User.UserStatus status, 
                                       @Param("startDate") java.time.LocalDateTime startDate);

    /**
     * 自定义原生SQL查询
     * 面试重点：原生SQL的使用场景、结果映射
     */
    @Query(value = "SELECT * FROM users WHERE first_name LIKE %:firstName% AND last_name LIKE %:lastName%", 
           nativeQuery = true)
    List<User> findByNameContaining(@Param("firstName") String firstName, 
                                   @Param("lastName") String lastName);

    /**
     * 统计用户数量
     * 面试重点：聚合查询、返回类型
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") User.UserStatus status);

    /**
     * 检查用户名是否存在
     * 面试重点：布尔查询、性能优化
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
}
