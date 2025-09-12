package com.learning.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus条件构造器
 * 2. 分页查询的实现
 * 3. 自定义查询方法
 * 4. 批量操作
 * 5. 统计查询
 * 
 * @author 学习笔记
 */
@Mapper
public interface OrderRepository extends BaseMapper<Order> {

    /**
     * 根据订单号查询订单
     * 面试重点：唯一字段查询
     */
    @Select("SELECT * FROM orders WHERE order_number = #{orderNumber} AND deleted = 0")
    Order findByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 根据用户ID查询订单列表
     * 面试重点：关联查询
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND deleted = 0 ORDER BY created_at DESC")
    List<Order> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    /**
     * 根据用户ID分页查询订单
     * 面试重点：分页查询
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND deleted = 0")
    IPage<Order> findByUserId(Page<Order> page, @Param("userId") Long userId);

    /**
     * 根据状态查询订单列表
     * 面试重点：枚举查询
     */
    @Select("SELECT * FROM orders WHERE status = #{status} AND deleted = 0")
    List<Order> findByStatus(@Param("status") String status);

    /**
     * 根据状态分页查询订单
     */
    @Select("SELECT * FROM orders WHERE status = #{status} AND deleted = 0")
    IPage<Order> findByStatus(Page<Order> page, @Param("status") String status);

    /**
     * 根据用户ID和状态查询订单
     * 面试重点：多条件查询
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND status = #{status} AND deleted = 0")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 根据创建时间范围查询订单
     * 面试重点：时间范围查询
     */
    @Select("SELECT * FROM orders WHERE created_at BETWEEN #{startTime} AND #{endTime} AND deleted = 0")
    List<Order> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据金额范围查询订单
     * 面试重点：数值范围查询
     */
    @Select("SELECT * FROM orders WHERE total_amount BETWEEN #{minAmount} AND #{maxAmount} AND deleted = 0")
    List<Order> findByTotalAmountBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    /**
     * 根据用户ID和创建时间范围查询订单
     * 面试重点：复杂条件查询
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND created_at BETWEEN #{startTime} AND #{endTime} AND deleted = 0")
    List<Order> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 自定义SQL查询 - 根据用户ID查询订单统计信息
     * 面试重点：SQL聚合查询
     */
    @Select("SELECT COUNT(*), SUM(total_amount), AVG(total_amount) FROM orders WHERE user_id = #{userId} AND deleted = 0")
    Object[] findOrderStatisticsByUserId(@Param("userId") Long userId);

    /**
     * 自定义SQL查询 - 根据状态查询订单数量
     * 面试重点：SQL统计查询
     */
    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status} AND deleted = 0")
    Long countByStatus(@Param("status") String status);

    /**
     * 自定义SQL查询 - 查询用户订单总金额
     * 面试重点：SQL聚合函数
     */
    @Select("SELECT SUM(total_amount) FROM orders WHERE user_id = #{userId} AND deleted = 0")
    BigDecimal sumTotalAmountByUserId(@Param("userId") Long userId);

    /**
     * 自定义SQL查询 - 查询订单详情（包含订单项）
     * 面试重点：JOIN查询
     */
    @Select("SELECT o.*, oi.id as item_id, oi.product_id, oi.product_name, oi.quantity, oi.price, oi.total_price " +
            "FROM orders o LEFT JOIN order_items oi ON o.id = oi.order_id " +
            "WHERE o.id = #{id} AND o.deleted = 0")
    Order findByIdWithOrderItems(@Param("id") Long id);

    /**
     * 自定义SQL查询 - 查询用户订单详情
     * 面试重点：JOIN查询
     */
    @Select("SELECT o.*, oi.id as item_id, oi.product_id, oi.product_name, oi.quantity, oi.price, oi.total_price " +
            "FROM orders o LEFT JOIN order_items oi ON o.id = oi.order_id " +
            "WHERE o.user_id = #{userId} AND o.deleted = 0 ORDER BY o.created_at DESC")
    List<Order> findByUserIdWithOrderItems(@Param("userId") Long userId);

    /**
     * 自定义SQL查询 - 查询订单统计信息
     * 面试重点：SQL查询
     */
    @Select("SELECT status, COUNT(*) as count, SUM(total_amount) as total FROM orders WHERE deleted = 0 GROUP BY status")
    List<Object[]> findOrderStatisticsByStatus();

    /**
     * 自定义SQL查询 - 查询用户订单排名
     * 面试重点：SQL排序查询
     */
    @Select("SELECT user_id, COUNT(*) as order_count, SUM(total_amount) as total_amount " +
            "FROM orders WHERE deleted = 0 GROUP BY user_id ORDER BY total_amount DESC LIMIT #{limit}")
    List<Object[]> findTopUsersByOrderAmount(@Param("limit") int limit);

    /**
     * 自定义SQL查询 - 查询月度订单统计
     * 面试重点：SQL时间函数
     */
    @Select("SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as count, SUM(total_amount) as total " +
            "FROM orders WHERE created_at >= #{startDate} AND deleted = 0 GROUP BY month ORDER BY month")
    List<Object[]> findMonthlyOrderStatistics(@Param("startDate") LocalDateTime startDate);

    /**
     * 更新订单状态
     * 面试重点：@Update注解的使用
     */
    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id} AND deleted = 0")
    int updateOrderStatus(@Param("id") Long id, @Param("status") String status, 
                         @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 批量更新订单状态
     * 面试重点：批量更新操作
     */
    @Update("<script>" +
            "UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach> AND deleted = 0" +
            "</script>")
    int batchUpdateOrderStatus(@Param("ids") List<Long> ids, @Param("status") String status, 
                              @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 删除过期订单（逻辑删除）
     * 面试重点：批量删除操作
     */
    @Update("UPDATE orders SET deleted = 1, updated_at = #{updatedAt} WHERE status = #{status} AND created_at < #{expiredTime} AND deleted = 0")
    int deleteExpiredOrders(@Param("status") String status, @Param("expiredTime") LocalDateTime expiredTime, 
                           @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 检查订单是否存在
     * 面试重点：存在性检查
     */
    @Select("SELECT COUNT(*) > 0 FROM orders WHERE order_number = #{orderNumber} AND deleted = 0")
    boolean existsByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 检查用户是否有订单
     * 面试重点：存在性检查
     */
    @Select("SELECT COUNT(*) > 0 FROM orders WHERE user_id = #{userId} AND deleted = 0")
    boolean existsByUserId(@Param("userId") Long userId);

    /**
     * 根据订单号检查订单是否存在
     * 面试重点：存在性检查
     */
    @Select("SELECT COUNT(*) > 0 FROM orders WHERE order_number = #{orderNumber} AND user_id = #{userId} AND deleted = 0")
    boolean existsByOrderNumberAndUserId(@Param("orderNumber") String orderNumber, @Param("userId") Long userId);
}
