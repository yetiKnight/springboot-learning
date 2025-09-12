package com.learning.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 事务回滚规则演示
 * 
 * 本类演示了 @Transactional 注解中 rollbackFor 参数的重要性
 * 
 * @author 学习笔记
 */
@Service
public class TransactionRollbackDemo {

    /**
     * 默认事务配置 - 只在 RuntimeException 时回滚
     * 问题：如果抛出 Checked Exception，事务不会回滚！
     */
    @Transactional
    public void defaultRollback() {
        // 模拟数据库操作
        System.out.println("执行数据库操作...");
        
        // 抛出 Checked Exception - 事务不会回滚！
        try {
            throw new IOException("文件操作失败");
        } catch (IOException e) {
            // 这里事务不会回滚，数据已经提交了！
            System.out.println("IOException 被捕获，但事务不会回滚");
        }
    }

    /**
     * 显式指定回滚规则 - 任何异常都会回滚
     * 推荐：明确指定回滚规则
     */
    @Transactional(rollbackFor = Exception.class)
    public void explicitRollback() {
        // 模拟数据库操作
        System.out.println("执行数据库操作...");
        
        // 抛出 Checked Exception - 事务会回滚！
        try {
            throw new SQLException("数据库操作失败");
        } catch (SQLException e) {
            // 这里事务会回滚，数据不会提交
            System.out.println("SQLException 被捕获，事务会回滚");
        }
    }

    /**
     * 只回滚特定异常
     * 精确控制：只对特定异常回滚
     */
    @Transactional(rollbackFor = {SQLException.class, IOException.class})
    public void specificRollback() {
        System.out.println("执行数据库操作...");
        
        // 只有 SQLException 和 IOException 会回滚
        // 其他异常不会回滚
    }

    /**
     * 不回滚特定异常
     * 特殊情况：某些异常不需要回滚
     */
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void noRollbackForSpecific() {
        System.out.println("执行数据库操作...");
        
        // IllegalArgumentException 不会回滚事务
        // 其他 RuntimeException 仍然会回滚
    }
}
