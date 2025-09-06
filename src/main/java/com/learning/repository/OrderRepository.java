package com.learning.repository;

import com.learning.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问层
 * 
 * 面试重点知识点：
 * 1. 复杂查询方法的编写
 * 2. 分页查询的实现
 * 3. 自定义查询方法
 * 4. 批量操作
 * 5. 统计查询
 * 
 * @author 学习笔记
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单号查询订单
     * 面试重点：唯一字段查询
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 根据用户ID查询订单列表
     * 面试重点：关联查询
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据用户ID分页查询订单
     * 面试重点：分页查询
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据状态查询订单列表
     * 面试重点：枚举查询
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * 根据状态分页查询订单
     */
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);

    /**
     * 根据用户ID和状态查询订单
     * 面试重点：多条件查询
     */
    List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);

    /**
     * 根据创建时间范围查询订单
     * 面试重点：时间范围查询
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据金额范围查询订单
     * 面试重点：数值范围查询
     */
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * 根据用户ID和创建时间范围查询订单
     * 面试重点：复杂条件查询
     */
    List<Order> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 自定义JPQL查询 - 根据用户ID查询订单统计信息
     * 面试重点：JPQL聚合查询
     */
    @Query("SELECT COUNT(o), SUM(o.totalAmount), AVG(o.totalAmount) FROM Order o WHERE o.userId = :userId")
    Object[] findOrderStatisticsByUserId(@Param("userId") Long userId);

    /**
     * 自定义JPQL查询 - 根据状态查询订单数量
     * 面试重点：JPQL统计查询
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") Order.OrderStatus status);

    /**
     * 自定义JPQL查询 - 查询用户订单总金额
     * 面试重点：JPQL聚合函数
     */
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.userId = :userId")
    BigDecimal sumTotalAmountByUserId(@Param("userId") Long userId);

    /**
     * 自定义JPQL查询 - 查询订单详情（包含订单项）
     * 面试重点：JOIN FETCH查询
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithOrderItems(@Param("id") Long id);

    /**
     * 自定义JPQL查询 - 查询用户订单详情
     * 面试重点：JOIN FETCH查询
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserIdWithOrderItems(@Param("userId") Long userId);

    /**
     * 自定义原生SQL查询 - 查询订单统计信息
     * 面试重点：原生SQL查询
     */
    @Query(value = "SELECT status, COUNT(*) as count, SUM(total_amount) as total FROM orders GROUP BY status", 
           nativeQuery = true)
    List<Object[]> findOrderStatisticsByStatus();

    /**
     * 自定义原生SQL查询 - 查询用户订单排名
     * 面试重点：原生SQL排序查询
     */
    @Query(value = "SELECT user_id, COUNT(*) as order_count, SUM(total_amount) as total_amount " +
                   "FROM orders GROUP BY user_id ORDER BY total_amount DESC LIMIT :limit", 
           nativeQuery = true)
    List<Object[]> findTopUsersByOrderAmount(@Param("limit") int limit);

    /**
     * 自定义原生SQL查询 - 查询月度订单统计
     * 面试重点：原生SQL时间函数
     */
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as count, SUM(total_amount) as total " +
                   "FROM orders WHERE created_at >= :startDate GROUP BY month ORDER BY month", 
           nativeQuery = true)
    List<Object[]> findMonthlyOrderStatistics(@Param("startDate") LocalDateTime startDate);

    /**
     * 更新订单状态
     * 面试重点：@Modifying注解的使用
     */
    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.updatedAt = :updatedAt WHERE o.id = :id")
    int updateOrderStatus(@Param("id") Long id, @Param("status") Order.OrderStatus status, 
                         @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 批量更新订单状态
     * 面试重点：批量更新操作
     */
    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.updatedAt = :updatedAt WHERE o.id IN :ids")
    int batchUpdateOrderStatus(@Param("ids") List<Long> ids, @Param("status") Order.OrderStatus status, 
                              @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 删除过期订单
     * 面试重点：批量删除操作
     */
    @Modifying
    @Query("DELETE FROM Order o WHERE o.status = :status AND o.createdAt < :expiredTime")
    int deleteExpiredOrders(@Param("status") Order.OrderStatus status, @Param("expiredTime") LocalDateTime expiredTime);

    /**
     * 检查订单是否存在
     * 面试重点：存在性检查
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * 检查用户是否有订单
     * 面试重点：存在性检查
     */
    boolean existsByUserId(Long userId);

    /**
     * 根据订单号检查订单是否存在
     * 面试重点：存在性检查
     */
    boolean existsByOrderNumberAndUserId(String orderNumber, Long userId);
}
