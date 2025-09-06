package com.learning.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单项实体类
 * 
 * 面试重点知识点：
 * 1. 多对一关系映射
 * 2. 外键约束
 * 3. 级联操作
 * 4. 实体验证
 * 
 * @author 学习笔记
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull(message = "商品名称不能为空")
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Min(value = 1, message = "商品数量必须大于0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "商品总价不能为负数")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 订单关系 - 多对一
     * 面试重点：@ManyToOne注解的使用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
}
