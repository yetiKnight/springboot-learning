package com.learning.service;

import com.learning.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 定时任务服务类
 * 
 * 面试重点知识点：
 * 1. @Scheduled注解的使用和配置
 * 2. Cron表达式的编写
 * 3. 定时任务的执行策略
 * 4. 定时任务的异常处理
 * 5. 定时任务的监控和管理
 * 
 * @author 学习笔记
 */
@Service
@Slf4j
public class ScheduledService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 固定延迟执行 - 每5秒执行一次
     * 面试重点：fixedDelay vs fixedRate的区别
     */
    @Scheduled(fixedDelay = 5000)
    @LogExecutionTime
    public void fixedDelayTask() {
        log.info("固定延迟任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("固定延迟任务被中断", e);
        }
    }

    /**
     * 固定频率执行 - 每3秒执行一次
     * 面试重点：fixedRate的执行机制
     */
    @Scheduled(fixedRate = 3000)
    @LogExecutionTime
    public void fixedRateTask() {
        log.info("固定频率任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("固定频率任务被中断", e);
        }
    }

    /**
     * 初始延迟执行 - 延迟10秒后开始，每2秒执行一次
     * 面试重点：initialDelay的使用
     */
    @Scheduled(initialDelay = 10000, fixedRate = 2000)
    @LogExecutionTime
    public void initialDelayTask() {
        log.info("初始延迟任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("初始延迟任务被中断", e);
        }
    }

    /**
     * Cron表达式任务 - 每分钟执行一次
     * 面试重点：Cron表达式的编写
     */
    @Scheduled(cron = "0 * * * * ?")
    @LogExecutionTime
    public void cronTask() {
        log.info("Cron任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Cron任务被中断", e);
        }
    }

    /**
     * 复杂Cron表达式任务 - 每天上午10点执行
     * 面试重点：复杂Cron表达式的编写
     */
    @Scheduled(cron = "0 0 10 * * ?")
    @LogExecutionTime
    public void dailyTask() {
        log.info("每日任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("每日任务被中断", e);
        }
    }

    /**
     * 工作日任务 - 周一到周五上午9点执行
     * 面试重点：工作日Cron表达式
     */
    @Scheduled(cron = "0 0 9 * * MON-FRI")
    @LogExecutionTime
    public void workdayTask() {
        log.info("工作日任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("工作日任务被中断", e);
        }
    }

    /**
     * 月末任务 - 每月最后一天执行
     * 面试重点：月末Cron表达式
     */
    @Scheduled(cron = "0 0 0 L * ?")
    @LogExecutionTime
    public void monthEndTask() {
        log.info("月末任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟任务执行
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("月末任务被中断", e);
        }
    }

    /**
     * 异常处理任务 - 演示定时任务的异常处理
     * 面试重点：定时任务的异常处理机制
     */
    @Scheduled(fixedRate = 10000)
    @LogExecutionTime
    public void exceptionTask() {
        log.info("异常处理任务执行，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟随机异常
            if (Math.random() > 0.7) {
                throw new RuntimeException("模拟定时任务异常");
            }
            log.info("异常处理任务执行成功");
        } catch (Exception e) {
            log.error("定时任务执行异常", e);
            // 这里可以添加异常处理逻辑，比如发送告警、记录日志等
        }
    }

    /**
     * 长时间运行任务 - 演示长时间运行的任务
     * 面试重点：长时间运行任务的处理
     */
    @Scheduled(fixedDelay = 30000)
    @LogExecutionTime
    public void longRunningTask() {
        log.info("长时间运行任务开始，时间：{}", LocalDateTime.now().format(formatter));
        try {
            // 模拟长时间运行的任务
            Thread.sleep(5000);
            log.info("长时间运行任务完成，时间：{}", LocalDateTime.now().format(formatter));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("长时间运行任务被中断", e);
        }
    }

    /**
     * 条件执行任务 - 根据条件决定是否执行
     * 面试重点：条件执行任务的设计
     */
    @Scheduled(fixedRate = 15000)
    @LogExecutionTime
    public void conditionalTask() {
        log.info("条件执行任务检查，时间：{}", LocalDateTime.now().format(formatter));
        
        // 模拟条件检查
        boolean shouldExecute = Math.random() > 0.3;
        
        if (shouldExecute) {
            log.info("条件满足，执行任务");
            try {
                // 模拟任务执行
                Thread.sleep(1000);
                log.info("条件任务执行完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("条件任务被中断", e);
            }
        } else {
            log.info("条件不满足，跳过任务执行");
        }
    }
}
