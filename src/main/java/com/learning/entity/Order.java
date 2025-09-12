package com.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus实体映射
 * 2. 一对多、多对一关系
 * 3. 级联操作和懒加载
 * 4. 审计字段的设计
 * 5. 实体验证注解
 * 
 * @author 学习笔记
 */
@TableName("orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "订单号不能为空")
    @TableField("order_number")
    private String orderNumber;

    @NotNull(message = "用户ID不能为空")
    @TableField("user_id")
    private Long userId;

    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private OrderStatus status = OrderStatus.PENDING;

    @TableField("remark")
    private String remark;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    /**
     * 订单项列表 - 一对多关系
     * 面试重点：MyBatis Plus中通过Service层处理关联关系
     */
    @TableField(exist = false)
    private List<OrderItem> orderItems;

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING,    // 待处理
        CONFIRMED,  // 已确认
        SHIPPED,    // 已发货
        DELIVERED,  // 已送达
        CANCELLED,  // 已取消
        REFUNDED    // 已退款
    }
}
