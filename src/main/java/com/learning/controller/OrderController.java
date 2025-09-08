package com.learning.controller;

import com.learning.entity.Order;
import com.learning.entity.OrderItem;
import com.learning.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订单控制器
 * 
 * 面试重点知识点：
 * 1. 复杂业务API的设计
 * 2. 分页查询的实现
 * 3. 批量操作的处理
 * 4. 统计查询的API设计
 * 5. 异常处理和状态码
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     * 面试重点：复杂业务API的设计
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("创建订单，用户ID：{}", request.getUserId());
        
        Order order = new Order();
        order.setOrderNumber(request.getOrderNumber());
        order.setUserId(request.getUserId());
        order.setRemark(request.getRemark());
        
        Order createdOrder = orderService.createOrder(order, request.getOrderItems());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * 根据ID查询订单
     * 面试重点：RESTful API设计
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("查询订单，ID：{}", id);
        Optional<Order> order = orderService.findById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据订单号查询订单
     * 面试重点：查询参数处理
     */
    @GetMapping("/search")
    public ResponseEntity<Order> getOrderByNumber(@RequestParam String orderNumber) {
        log.info("查询订单，订单号：{}", orderNumber);
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询订单详情（包含订单项）
     * 面试重点：关联数据查询
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long id) {
        log.info("查询订单详情，ID：{}", id);
        Optional<Order> order = orderService.findByIdWithOrderItems(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询用户订单列表
     * 面试重点：分页查询API
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Order>> getUserOrders(@PathVariable Long userId, Pageable pageable) {
        log.info("查询用户订单列表，用户ID：{}", userId);
        Page<Order> orders = orderService.findByUserId(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * 更新订单状态
     * 面试重点：状态更新API
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, 
                                                  @RequestParam Order.OrderStatus status) {
        log.info("更新订单状态，ID：{}，状态：{}", id, status);
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 批量更新订单状态
     * 面试重点：批量操作API
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Map<String, Object>> batchUpdateOrderStatus(
            @RequestParam List<Long> ids, 
            @RequestParam Order.OrderStatus status) {
        log.info("批量更新订单状态，订单数量：{}，状态：{}", ids.size(), status);
        try {
            int updatedCount = orderService.batchUpdateOrderStatus(ids, status);
            Map<String, Object> result = new HashMap<>();
            result.put("updatedCount", updatedCount);
            result.put("totalCount", ids.size());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 取消订单
     * 面试重点：业务操作API
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        log.info("取消订单，ID：{}", id);
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除订单
     * 面试重点：删除操作API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("删除订单，ID：{}", id);
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 查询订单统计信息
     * 面试重点：统计查询API
     */
    @GetMapping("/statistics/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrderStatistics(@PathVariable Long userId) {
        log.info("查询订单统计信息，用户ID：{}", userId);
        Object[] stats = orderService.getOrderStatistics(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("orderCount", stats[0]);
        result.put("totalAmount", stats[1]);
        result.put("averageAmount", stats[2]);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 查询订单总金额
     * 面试重点：聚合查询API
     */
    @GetMapping("/total-amount/user/{userId}")
    public ResponseEntity<Map<String, Object>> getTotalOrderAmount(@PathVariable Long userId) {
        log.info("查询订单总金额，用户ID：{}", userId);
        BigDecimal totalAmount = orderService.getTotalOrderAmount(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("totalAmount", totalAmount);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 查询订单状态统计
     * 面试重点：分组统计API
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<List<Map<String, Object>>> getOrderStatusStatistics() {
        log.info("查询订单状态统计");
        List<Object[]> stats = orderService.getOrderStatusStatistics();
        
        List<Map<String, Object>> result = stats.stream()
                .map(stat -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("status", stat[0]);
                    item.put("count", stat[1]);
                    item.put("totalAmount", stat[2]);
                    return item;
                })
                .toList();
        
        return ResponseEntity.ok(result);
    }

    /**
     * 创建订单请求DTO
     */
    @lombok.Data
    public static class CreateOrderRequest {
        private String orderNumber;
        private Long userId;
        private String remark;
        private List<OrderItem> orderItems;
    }
}
