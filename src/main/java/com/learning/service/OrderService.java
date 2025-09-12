package com.learning.service;

import com.learning.annotation.LogExecutionTime;
import com.learning.entity.Order;
import com.learning.entity.OrderItem;
import com.learning.repository.OrderRepository;
import com.learning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务类
 * 
 * 面试重点知识点：
 * 1. 复杂业务逻辑的事务管理
 * 2. 事务传播机制的使用
 * 3. 异常处理和事务回滚
 * 4. 缓存策略的应用
 * 5. 业务逻辑的封装
 * 
 * @author 学习笔记
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * 创建订单
     * 面试重点：复杂业务逻辑的事务管理
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orders", allEntries = true)
    @LogExecutionTime(logArgs = true, logResult = true)
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        log.info("创建订单，订单号：{}", order.getOrderNumber());
        
        // 1. 验证用户是否存在
        if (userRepository.selectById(order.getUserId()) == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 验证订单号是否重复
        if (orderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new RuntimeException("订单号已存在");
        }
        
        // 3. 计算订单总金额
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        
        // 4. 保存订单
        orderRepository.insert(order);
        Order savedOrder = order;
        
        // 5. 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(savedOrder.getId());
            item.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        
        log.info("订单创建成功，订单ID：{}", savedOrder.getId());
        return savedOrder;
    }

    /**
     * 根据ID查询订单
     * 面试重点：缓存策略的应用
     */
    @Cacheable(value = "orders", key = "#id")
    @LogExecutionTime(logArgs = true, logResult = true)
    public Order findById(Long id) {
        log.info("查询订单，ID：{}", id);
        return orderRepository.selectById(id);
    }

    /**
     * 根据订单号查询订单
     * 面试重点：缓存策略的应用
     */
    @Cacheable(value = "orders", key = "#orderNumber")
    @LogExecutionTime(logArgs = true, logResult = true)
    public Order findByOrderNumber(String orderNumber) {
        log.info("查询订单，订单号：{}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber);
    }

    /**
     * 查询用户订单列表
     * 面试重点：分页查询
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public IPage<Order> findByUserId(Long userId, Page<Order> page) {
        log.info("查询用户订单列表，用户ID：{}", userId);
        return orderRepository.findByUserId(page, userId);
    }

    /**
     * 查询订单详情（包含订单项）
     * 面试重点：JOIN查询
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public Order findByIdWithOrderItems(Long id) {
        log.info("查询订单详情，ID：{}", id);
        return orderRepository.findByIdWithOrderItems(id);
    }

    /**
     * 更新订单状态
     * 面试重点：事务传播机制
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orders", key = "#id")
    @LogExecutionTime(logArgs = true, logResult = true)
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        log.info("更新订单状态，ID：{}，状态：{}", id, status);
        
        Order order = orderRepository.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 验证状态转换是否合法
        if (!isValidStatusTransition(order.getStatus(), status)) {
            throw new RuntimeException("无效的状态转换");
        }
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepository.updateById(order);
        log.info("订单状态更新成功，ID：{}", id);
        
        return order;
    }

    /**
     * 批量更新订单状态
     * 面试重点：批量操作
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orders", allEntries = true)
    @LogExecutionTime(logArgs = true, logResult = true)
    public int batchUpdateOrderStatus(List<Long> ids, Order.OrderStatus status) {
        log.info("批量更新订单状态，订单数量：{}，状态：{}", ids.size(), status);
        
        int updatedCount = orderRepository.batchUpdateOrderStatus(ids, status.toString(), LocalDateTime.now());
        log.info("批量更新订单状态完成，更新数量：{}", updatedCount);
        
        return updatedCount;
    }

    /**
     * 取消订单
     * 面试重点：业务逻辑的封装
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orders", key = "#id")
    @LogExecutionTime(logArgs = true, logResult = true)
    public Order cancelOrder(Long id) {
        log.info("取消订单，ID：{}", id);
        
        Order order = orderRepository.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 验证订单是否可以取消
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        // 更新订单状态
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepository.updateById(order);
        log.info("订单取消成功，ID：{}", id);
        
        return order;
    }

    /**
     * 删除订单
     * 面试重点：软删除 vs 硬删除
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "orders", key = "#id")
    @LogExecutionTime(logArgs = true)
    public void deleteOrder(Long id) {
        log.info("删除订单，ID：{}", id);
        
        Order order = orderRepository.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 验证订单是否可以删除
        if (order.getStatus() == Order.OrderStatus.SHIPPED || 
            order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("订单状态不允许删除");
        }
        
        orderRepository.deleteById(id);
        log.info("订单删除成功，ID：{}", id);
    }

    /**
     * 查询订单统计信息
     * 面试重点：统计查询
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public Object[] getOrderStatistics(Long userId) {
        log.info("查询订单统计信息，用户ID：{}", userId);
        return orderRepository.findOrderStatisticsByUserId(userId);
    }

    /**
     * 查询订单金额统计
     * 面试重点：聚合查询
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public BigDecimal getTotalOrderAmount(Long userId) {
        log.info("查询订单总金额，用户ID：{}", userId);
        return orderRepository.sumTotalAmountByUserId(userId);
    }

    /**
     * 查询订单状态统计
     * 面试重点：分组统计查询
     */
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<Object[]> getOrderStatusStatistics() {
        log.info("查询订单状态统计");
        return orderRepository.findOrderStatisticsByStatus();
    }

    /**
     * 验证状态转换是否合法
     * 面试重点：业务规则验证
     */
    private boolean isValidStatusTransition(Order.OrderStatus from, Order.OrderStatus to) {
        // 定义状态转换规则
        switch (from) {
            case PENDING:
                return to == Order.OrderStatus.CONFIRMED || to == Order.OrderStatus.CANCELLED;
            case CONFIRMED:
                return to == Order.OrderStatus.SHIPPED || to == Order.OrderStatus.CANCELLED;
            case SHIPPED:
                return to == Order.OrderStatus.DELIVERED;
            case DELIVERED:
                return to == Order.OrderStatus.REFUNDED;
            case CANCELLED:
            case REFUNDED:
                return false; // 终态，不能转换
            default:
                return false;
        }
    }
}
