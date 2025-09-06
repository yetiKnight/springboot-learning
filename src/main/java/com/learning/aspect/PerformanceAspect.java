package com.learning.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能监控切面
 * 
 * 面试重点知识点：
 * 1. AOP切面的高级用法
 * 2. 性能监控的实现
 * 3. 切点表达式的编写
 * 4. 环绕通知的使用
 * 5. 性能数据的收集和分析
 * 
 * @author 学习笔记
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    // 性能统计数据
    private final ConcurrentHashMap<String, MethodPerformance> performanceMap = new ConcurrentHashMap<>();
    private final AtomicLong totalCalls = new AtomicLong(0);

    /**
     * 定义切点 - 所有Service层的方法
     * 面试重点：切点表达式的编写
     */
    @Pointcut("execution(* com.learning.service.*.*(..))")
    public void serviceLayer() {}

    /**
     * 定义切点 - 所有Controller层的方法
     */
    @Pointcut("execution(* com.learning.controller.*.*(..))")
    public void controllerLayer() {}

    /**
     * 定义切点 - 所有Repository层的方法
     */
    @Pointcut("execution(* com.learning.repository.*.*(..))")
    public void repositoryLayer() {}

    /**
     * 性能监控 - Service层
     * 面试重点：环绕通知的使用
     */
    @Around("serviceLayer()")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "SERVICE");
    }

    /**
     * 性能监控 - Controller层
     */
    @Around("controllerLayer()")
    public Object monitorControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "CONTROLLER");
    }

    /**
     * 性能监控 - Repository层
     */
    @Around("repositoryLayer()")
    public Object monitorRepositoryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "REPOSITORY");
    }

    /**
     * 通用性能监控方法
     * 面试重点：性能监控的实现
     */
    private Object monitorPerformance(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String key = layer + ":" + methodName;
        
        long startTime = System.currentTimeMillis();
        long startMemory = getUsedMemory();
        
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录成功执行的性能数据
            recordPerformance(key, methodName, layer, startTime, startMemory, true, null);
            
            return result;
        } catch (Throwable throwable) {
            // 记录异常执行的性能数据
            recordPerformance(key, methodName, layer, startTime, startMemory, false, throwable);
            throw throwable;
        }
    }

    /**
     * 记录性能数据
     * 面试重点：性能数据的收集
     */
    private void recordPerformance(String key, String methodName, String layer, 
                                 long startTime, long startMemory, boolean success, Throwable throwable) {
        long endTime = System.currentTimeMillis();
        long endMemory = getUsedMemory();
        
        long executionTime = endTime - startTime;
        long memoryUsed = endMemory - startMemory;
        
        // 更新性能统计
        MethodPerformance performance = performanceMap.computeIfAbsent(key, 
            k -> new MethodPerformance(methodName, layer));
        
        performance.recordExecution(executionTime, memoryUsed, success);
        totalCalls.incrementAndGet();
        
        // 记录日志
        if (success) {
            log.debug("方法执行完成 - {}: {}, 执行时间: {}ms, 内存使用: {}KB", 
                     layer, methodName, executionTime, memoryUsed / 1024);
        } else {
            log.error("方法执行异常 - {}: {}, 执行时间: {}ms, 异常: {}", 
                     layer, methodName, executionTime, throwable.getMessage());
        }
        
        // 性能告警
        if (executionTime > 1000) { // 超过1秒
            log.warn("性能告警 - {}: {} 执行时间过长: {}ms", layer, methodName, executionTime);
        }
        
        if (memoryUsed > 10 * 1024 * 1024) { // 超过10MB
            log.warn("内存告警 - {}: {} 内存使用过多: {}KB", layer, methodName, memoryUsed / 1024);
        }
    }

    /**
     * 获取当前使用的内存
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * 获取性能统计信息
     * 面试重点：性能数据的分析
     */
    public ConcurrentHashMap<String, MethodPerformance> getPerformanceStatistics() {
        return new ConcurrentHashMap<>(performanceMap);
    }

    /**
     * 获取总调用次数
     */
    public long getTotalCalls() {
        return totalCalls.get();
    }

    /**
     * 清除性能统计
     */
    public void clearStatistics() {
        performanceMap.clear();
        totalCalls.set(0);
    }

    /**
     * 方法性能统计类
     */
    public static class MethodPerformance {
        private final String methodName;
        private final String layer;
        private final AtomicLong callCount = new AtomicLong(0);
        private final AtomicLong successCount = new AtomicLong(0);
        private final AtomicLong failureCount = new AtomicLong(0);
        private final AtomicLong totalExecutionTime = new AtomicLong(0);
        private final AtomicLong totalMemoryUsed = new AtomicLong(0);
        private final AtomicLong maxExecutionTime = new AtomicLong(0);
        private final AtomicLong minExecutionTime = new AtomicLong(Long.MAX_VALUE);

        public MethodPerformance(String methodName, String layer) {
            this.methodName = methodName;
            this.layer = layer;
        }

        public void recordExecution(long executionTime, long memoryUsed, boolean success) {
            callCount.incrementAndGet();
            
            if (success) {
                successCount.incrementAndGet();
            } else {
                failureCount.incrementAndGet();
            }
            
            totalExecutionTime.addAndGet(executionTime);
            totalMemoryUsed.addAndGet(memoryUsed);
            
            // 更新最大执行时间
            long currentMax = maxExecutionTime.get();
            while (executionTime > currentMax && !maxExecutionTime.compareAndSet(currentMax, executionTime)) {
                currentMax = maxExecutionTime.get();
            }
            
            // 更新最小执行时间
            long currentMin = minExecutionTime.get();
            while (executionTime < currentMin && !minExecutionTime.compareAndSet(currentMin, executionTime)) {
                currentMin = minExecutionTime.get();
            }
        }

        // Getters
        public String getMethodName() { return methodName; }
        public String getLayer() { return layer; }
        public long getCallCount() { return callCount.get(); }
        public long getSuccessCount() { return successCount.get(); }
        public long getFailureCount() { return failureCount.get(); }
        public long getTotalExecutionTime() { return totalExecutionTime.get(); }
        public long getTotalMemoryUsed() { return totalMemoryUsed.get(); }
        public long getMaxExecutionTime() { return maxExecutionTime.get(); }
        public long getMinExecutionTime() { return minExecutionTime.get() == Long.MAX_VALUE ? 0 : minExecutionTime.get(); }
        
        public double getAverageExecutionTime() {
            long calls = callCount.get();
            return calls > 0 ? (double) totalExecutionTime.get() / calls : 0;
        }
        
        public double getSuccessRate() {
            long calls = callCount.get();
            return calls > 0 ? (double) successCount.get() / calls * 100 : 0;
        }
        
        public double getAverageMemoryUsed() {
            long calls = callCount.get();
            return calls > 0 ? (double) totalMemoryUsed.get() / calls : 0;
        }
    }
}
