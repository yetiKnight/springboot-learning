package com.learning.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 * 
 * 面试重点知识点：
 * 1. JPA实体关系映射
 * 2. 一对多、多对一关系
 * 3. 级联操作和懒加载
 * 4. 审计字段的设计
 * 5. 实体验证注解
 * 
 * @author 学习笔记
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "订单号不能为空")
    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "remark", length = 500)
    private String remark;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 订单项列表 - 一对多关系
     * 面试重点：@OneToMany注解的使用
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
