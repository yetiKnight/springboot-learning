package com.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus实体映射注解
 * 2. 字段验证注解
 * 3. Lombok注解的使用
 * 4. 审计字段的设计
 * 5. 逻辑删除配置
 * 
 * @author 学习笔记
 */
@TableName("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @TableField("username")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    @TableField("password")
    private String password;

    @Email(message = "邮箱格式不正确")
    @TableField("email")
    private String email;

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    @TableField("status")
    private UserStatus status = UserStatus.ACTIVE;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        ACTIVE, INACTIVE, LOCKED, DELETED
    }
}
