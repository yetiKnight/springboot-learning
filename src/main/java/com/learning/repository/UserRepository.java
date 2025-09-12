package com.learning.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问层
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus BaseMapper接口的作用和继承关系
 * 2. 方法名查询的规则和实现原理
 * 3. @Select注解的使用：原生SQL查询
 * 4. 参数绑定的方式：@Param注解
 * 5. 分页和排序的实现
 * 
 * @author 学习笔记
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 面试重点：MyBatis Plus条件构造器
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM users WHERE email = #{email} AND deleted = 0")
    User findByEmail(@Param("email") String email);

    /**
     * 根据状态查询用户列表
     */
    @Select("SELECT * FROM users WHERE status = #{status} AND deleted = 0")
    List<User> findByStatus(@Param("status") String status);

    /**
     * 根据用户名模糊查询
     * 面试重点：Like查询的写法
     */
    @Select("SELECT * FROM users WHERE username LIKE CONCAT('%', #{username}, '%') AND deleted = 0")
    List<User> findByUsernameContaining(@Param("username") String username);

    /**
     * 根据邮箱模糊查询
     */
    @Select("SELECT * FROM users WHERE email LIKE CONCAT('%', #{email}, '%') AND deleted = 0")
    List<User> findByEmailContaining(@Param("email") String email);

    /**
     * 自定义原生SQL查询
     * 面试重点：原生SQL的使用场景、结果映射
     */
    @Select("SELECT * FROM users WHERE status = #{status} AND created_at >= #{startDate} AND deleted = 0")
    List<User> findActiveUsersAfterDate(@Param("status") String status, 
                                       @Param("startDate") LocalDateTime startDate);

    /**
     * 根据姓名模糊查询
     * 面试重点：原生SQL的使用场景、结果映射
     */
    @Select("SELECT * FROM users WHERE first_name LIKE CONCAT('%', #{firstName}, '%') AND last_name LIKE CONCAT('%', #{lastName}, '%') AND deleted = 0")
    List<User> findByNameContaining(@Param("firstName") String firstName, 
                                   @Param("lastName") String lastName);

    /**
     * 统计用户数量
     * 面试重点：聚合查询、返回类型
     */
    @Select("SELECT COUNT(*) FROM users WHERE status = #{status} AND deleted = 0")
    Long countByStatus(@Param("status") String status);

    /**
     * 检查用户名是否存在
     * 面试重点：布尔查询、性能优化
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username} AND deleted = 0")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email} AND deleted = 0")
    boolean existsByEmail(@Param("email") String email);
}
