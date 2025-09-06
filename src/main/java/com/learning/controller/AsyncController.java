package com.learning.controller;

import com.learning.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 异步处理控制器
 * 
 * 面试重点知识点：
 * 1. 异步方法的调用和返回值处理
 * 2. CompletableFuture的使用
 * 3. 异步异常处理
 * 4. 异步方法的适用场景
 * 5. 性能优化和用户体验提升
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/async")
@RequiredArgsConstructor
@Slf4j
public class AsyncController {

    private final AsyncService asyncService;

    /**
     * 异步方法调用 - 无返回值
     * 面试重点：异步方法的调用方式
     */
    @PostMapping("/method")
    public ResponseEntity<String> callAsyncMethod() {
        log.info("调用异步方法");
        asyncService.asyncMethod();
        return ResponseEntity.ok("异步方法已启动");
    }

    /**
     * 异步方法调用 - 有返回值
     * 面试重点：Future的使用
     */
    @PostMapping("/method-with-return")
    public ResponseEntity<String> callAsyncMethodWithReturn() {
        log.info("调用异步方法（有返回值）");
        try {
            Future<String> future = asyncService.asyncMethodWithReturn();
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("异步方法已启动，请稍后查询结果");
        } catch (Exception e) {
            log.error("调用异步方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }

    /**
     * 异步方法调用 - CompletableFuture
     * 面试重点：CompletableFuture的使用
     */
    @PostMapping("/method-with-completable-future")
    public ResponseEntity<String> callAsyncMethodWithCompletableFuture() {
        log.info("调用CompletableFuture异步方法");
        try {
            CompletableFuture<String> future = asyncService.asyncMethodWithCompletableFuture();
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("CompletableFuture异步方法已启动，请稍后查询结果");
        } catch (Exception e) {
            log.error("调用CompletableFuture异步方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }

    /**
     * 异步方法调用 - 异常处理
     * 面试重点：异步方法的异常处理
     */
    @PostMapping("/method-with-exception")
    public ResponseEntity<String> callAsyncMethodWithException() {
        log.info("调用异步异常方法");
        try {
            CompletableFuture<String> future = asyncService.asyncMethodWithException();
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("异步异常方法已启动，请稍后查询结果");
        } catch (Exception e) {
            log.error("调用异步异常方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }

    /**
     * 异步方法调用 - 链式操作
     * 面试重点：CompletableFuture的链式操作
     */
    @PostMapping("/method-chain")
    public ResponseEntity<String> callAsyncMethodChain() {
        log.info("调用异步链式方法");
        try {
            CompletableFuture<String> future = asyncService.asyncMethodChain()
                    .thenApply(result -> result + " -> 链式处理1")
                    .thenApply(result -> result + " -> 链式处理2")
                    .thenApply(result -> result + " -> 链式处理3");
            
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("异步链式方法已启动，请稍后查询结果");
        } catch (Exception e) {
            log.error("调用异步链式方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }

    /**
     * 异步方法调用 - 批量处理
     * 面试重点：异步方法的批量处理场景
     */
    @PostMapping("/batch-process")
    public ResponseEntity<String> callAsyncBatchProcess(@RequestParam List<String> dataList) {
        log.info("调用异步批量处理方法，数据量：{}", dataList.size());
        try {
            List<CompletableFuture<String>> futures = new ArrayList<>();
            
            // 启动多个异步任务
            for (String data : dataList) {
                CompletableFuture<String> future = asyncService.asyncBatchProcess(data);
                futures.add(future);
            }
            
            // 等待所有任务完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );
            
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("异步批量处理方法已启动，处理数据量：" + dataList.size());
        } catch (Exception e) {
            log.error("调用异步批量处理方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }

    /**
     * 异步方法调用 - 组合操作
     * 面试重点：CompletableFuture的组合操作
     */
    @PostMapping("/combine")
    public ResponseEntity<String> callAsyncCombine() {
        log.info("调用异步组合方法");
        try {
            CompletableFuture<String> future1 = asyncService.asyncMethodWithCompletableFuture();
            CompletableFuture<String> future2 = asyncService.asyncMethodChain();
            
            // 组合两个异步任务的结果
            CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (result1, result2) -> {
                return "组合结果：" + result1 + " + " + result2;
            });
            
            // 这里应该等待结果，但为了演示，直接返回
            return ResponseEntity.ok("异步组合方法已启动，请稍后查询结果");
        } catch (Exception e) {
            log.error("调用异步组合方法失败", e);
            return ResponseEntity.internalServerError().body("调用异步方法失败");
        }
    }
}
