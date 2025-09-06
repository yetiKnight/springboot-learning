package com.learning.service;

import com.learning.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 异步服务类
 * 
 * 面试重点知识点：
 * 1. @Async注解的使用和原理
 * 2. 异步方法的返回值处理
 * 3. 线程池配置和调优
 * 4. 异步异常处理
 * 5. 异步方法的适用场景
 * 
 * @author 学习笔记
 */
@Service
@Slf4j
public class AsyncService {

    /**
     * 异步方法 - 无返回值
     * 面试重点：@Async注解的使用
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public void asyncMethod() {
        log.info("异步方法开始执行，线程：{}", Thread.currentThread().getName());
        try {
            // 模拟耗时操作
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("异步方法被中断", e);
        }
        log.info("异步方法执行完成，线程：{}", Thread.currentThread().getName());
    }

    /**
     * 异步方法 - 有返回值
     * 面试重点：Future和CompletableFuture的使用
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public Future<String> asyncMethodWithReturn() {
        log.info("异步方法开始执行，线程：{}", Thread.currentThread().getName());
        try {
            // 模拟耗时操作
            Thread.sleep(2000);
            String result = "异步方法执行完成";
            log.info("异步方法执行完成，线程：{}", Thread.currentThread().getName());
            return new AsyncResult<>(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("异步方法被中断", e);
            return new AsyncResult<>("执行失败");
        }
    }

    /**
     * 异步方法 - 使用CompletableFuture
     * 面试重点：CompletableFuture的高级用法
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public CompletableFuture<String> asyncMethodWithCompletableFuture() {
        log.info("CompletableFuture异步方法开始执行，线程：{}", Thread.currentThread().getName());
        try {
            // 模拟耗时操作
            Thread.sleep(1500);
            String result = "CompletableFuture异步方法执行完成";
            log.info("CompletableFuture异步方法执行完成，线程：{}", Thread.currentThread().getName());
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("CompletableFuture异步方法被中断", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 异步方法 - 异常处理
     * 面试重点：异步方法的异常处理机制
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public CompletableFuture<String> asyncMethodWithException() {
        log.info("异步异常方法开始执行，线程：{}", Thread.currentThread().getName());
        try {
            // 模拟耗时操作
            Thread.sleep(1000);
            // 模拟异常
            if (Math.random() > 0.5) {
                throw new RuntimeException("模拟异步方法异常");
            }
            String result = "异步异常方法执行完成";
            log.info("异步异常方法执行完成，线程：{}", Thread.currentThread().getName());
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            log.error("异步异常方法执行失败", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 异步方法 - 链式调用
     * 面试重点：CompletableFuture的链式操作
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public CompletableFuture<String> asyncMethodChain() {
        log.info("异步链式方法开始执行，线程：{}", Thread.currentThread().getName());
        try {
            // 模拟耗时操作
            Thread.sleep(1000);
            String result = "异步链式方法执行完成";
            log.info("异步链式方法执行完成，线程：{}", Thread.currentThread().getName());
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("异步链式方法被中断", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 异步方法 - 批量处理
     * 面试重点：异步方法的批量处理场景
     */
    @Async("taskExecutor")
    @LogExecutionTime
    public CompletableFuture<String> asyncBatchProcess(String data) {
        log.info("异步批量处理方法开始执行，数据：{}，线程：{}", data, Thread.currentThread().getName());
        try {
            // 模拟批量处理
            Thread.sleep(500);
            String result = "批量处理完成：" + data;
            log.info("异步批量处理方法执行完成，线程：{}", Thread.currentThread().getName());
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("异步批量处理方法被中断", e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
