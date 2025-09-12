package com.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单项实体类
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus实体映射
 * 2. 外键约束
 * 3. 级联操作
 * 4. 实体验证
 * 
 * @author 学习笔记
 */
@TableName("order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @TableField("order_id")
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @TableField("product_id")
    private Long productId;

    @NotNull(message = "商品名称不能为空")
    @TableField("product_name")
    private String productName;

    @Min(value = 1, message = "商品数量必须大于0")
    @TableField("quantity")
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @TableField("price")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "商品总价不能为负数")
    @TableField("total_price")
    private BigDecimal totalPrice;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    /**
     * 订单关系 - 多对一
     * 面试重点：MyBatis Plus中通过Service层处理关联关系
     */
    @TableField(exist = false)
    private Order order;
}
