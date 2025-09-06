package com.learning.controller;

import com.learning.aspect.PerformanceAspect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 性能监控控制器
 * 
 * 面试重点知识点：
 * 1. 性能监控API的设计
 * 2. 性能数据的展示
 * 3. 性能问题的诊断
 * 4. 性能优化建议
 * 5. 实时监控的实现
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
@Slf4j
public class PerformanceController {

    private final PerformanceAspect performanceAspect;

    /**
     * 获取性能统计信息
     * 面试重点：性能监控API
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPerformanceStatistics() {
        log.info("获取性能统计信息");
        
        ConcurrentHashMap<String, PerformanceAspect.MethodPerformance> stats = 
            performanceAspect.getPerformanceStatistics();
        
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("totalCalls", performanceAspect.getTotalCalls());
        result.put("methodCount", stats.size());
        
        // 按层级分组统计
        Map<String, Object> layerStats = new HashMap<>();
        stats.values().forEach(performance -> {
            String layer = performance.getLayer();
            layerStats.compute(layer, (k, v) -> {
                if (v == null) {
                    Map<String, Object> layerData = new HashMap<>();
                    layerData.put("methodCount", 0);
                    layerData.put("totalCalls", 0L);
                    layerData.put("totalExecutionTime", 0L);
                    layerData.put("averageExecutionTime", 0.0);
                    layerData.put("successRate", 0.0);
                    return layerData;
                }
                return v;
            });
            
            Map<String, Object> layerData = (Map<String, Object>) layerStats.get(layer);
            layerData.put("methodCount", (Integer) layerData.get("methodCount") + 1);
            layerData.put("totalCalls", (Long) layerData.get("totalCalls") + performance.getCallCount());
            layerData.put("totalExecutionTime", (Long) layerData.get("totalExecutionTime") + performance.getTotalExecutionTime());
        });
        
        // 计算平均值
        layerStats.forEach((layer, data) -> {
            Map<String, Object> layerData = (Map<String, Object>) data;
            long totalCalls = (Long) layerData.get("totalCalls");
            long totalExecutionTime = (Long) layerData.get("totalExecutionTime");
            
            if (totalCalls > 0) {
                layerData.put("averageExecutionTime", (double) totalExecutionTime / totalCalls);
            }
        });
        
        result.put("layerStatistics", layerStats);
        result.put("methodStatistics", stats);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取方法性能详情
     * 面试重点：详细性能分析
     */
    @GetMapping("/method/{methodName}")
    public ResponseEntity<Map<String, Object>> getMethodPerformance(@PathVariable String methodName) {
        log.info("获取方法性能详情，方法名：{}", methodName);
        
        ConcurrentHashMap<String, PerformanceAspect.MethodPerformance> stats = 
            performanceAspect.getPerformanceStatistics();
        
        PerformanceAspect.MethodPerformance performance = stats.values().stream()
            .filter(p -> p.getMethodName().contains(methodName))
            .findFirst()
            .orElse(null);
        
        if (performance == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("methodName", performance.getMethodName());
        result.put("layer", performance.getLayer());
        result.put("callCount", performance.getCallCount());
        result.put("successCount", performance.getSuccessCount());
        result.put("failureCount", performance.getFailureCount());
        result.put("successRate", performance.getSuccessRate());
        result.put("totalExecutionTime", performance.getTotalExecutionTime());
        result.put("averageExecutionTime", performance.getAverageExecutionTime());
        result.put("maxExecutionTime", performance.getMaxExecutionTime());
        result.put("minExecutionTime", performance.getMinExecutionTime());
        result.put("totalMemoryUsed", performance.getTotalMemoryUsed());
        result.put("averageMemoryUsed", performance.getAverageMemoryUsed());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取性能排名
     * 面试重点：性能排序分析
     */
    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> getPerformanceRanking(@RequestParam(defaultValue = "executionTime") String sortBy) {
        log.info("获取性能排名，排序字段：{}", sortBy);
        
        ConcurrentHashMap<String, PerformanceAspect.MethodPerformance> stats = 
            performanceAspect.getPerformanceStatistics();
        
        // 根据排序字段排序
        var sortedStats = stats.values().stream()
            .sorted((p1, p2) -> {
                switch (sortBy) {
                    case "executionTime":
                        return Double.compare(p2.getAverageExecutionTime(), p1.getAverageExecutionTime());
                    case "callCount":
                        return Long.compare(p2.getCallCount(), p1.getCallCount());
                    case "memoryUsed":
                        return Double.compare(p2.getAverageMemoryUsed(), p1.getAverageMemoryUsed());
                    case "successRate":
                        return Double.compare(p1.getSuccessRate(), p2.getSuccessRate());
                    default:
                        return 0;
                }
            })
            .limit(10)
            .toList();
        
        Map<String, Object> result = new HashMap<>();
        result.put("sortBy", sortBy);
        result.put("ranking", sortedStats);
        result.put("totalMethods", stats.size());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取性能告警
     * 面试重点：性能告警机制
     */
    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getPerformanceAlerts() {
        log.info("获取性能告警");
        
        ConcurrentHashMap<String, PerformanceAspect.MethodPerformance> stats = 
            performanceAspect.getPerformanceStatistics();
        
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        
        // 性能告警规则
        var alerts = stats.values().stream()
            .filter(performance -> {
                double avgExecutionTime = performance.getAverageExecutionTime();
                double successRate = performance.getSuccessRate();
                long callCount = performance.getCallCount();
                
                return avgExecutionTime > 1000 || // 平均执行时间超过1秒
                       successRate < 95.0 ||     // 成功率低于95%
                       callCount > 1000;         // 调用次数过多
            })
            .map(performance -> {
                Map<String, Object> alert = new HashMap<>();
                alert.put("methodName", performance.getMethodName());
                alert.put("layer", performance.getLayer());
                alert.put("averageExecutionTime", performance.getAverageExecutionTime());
                alert.put("successRate", performance.getSuccessRate());
                alert.put("callCount", performance.getCallCount());
                
                // 告警原因
                StringBuilder reasons = new StringBuilder();
                if (performance.getAverageExecutionTime() > 1000) {
                    reasons.append("执行时间过长; ");
                }
                if (performance.getSuccessRate() < 95.0) {
                    reasons.append("成功率过低; ");
                }
                if (performance.getCallCount() > 1000) {
                    reasons.append("调用次数过多; ");
                }
                alert.put("reasons", reasons.toString());
                
                return alert;
            })
            .toList();
        
        result.put("alerts", alerts);
        result.put("alertCount", alerts.size());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 清除性能统计
     * 面试重点：性能数据管理
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearPerformanceStatistics() {
        log.info("清除性能统计");
        
        performanceAspect.clearStatistics();
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "性能统计已清除");
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取性能摘要
     * 面试重点：性能摘要信息
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getPerformanceSummary() {
        log.info("获取性能摘要");
        
        ConcurrentHashMap<String, PerformanceAspect.MethodPerformance> stats = 
            performanceAspect.getPerformanceStatistics();
        
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("totalCalls", performanceAspect.getTotalCalls());
        result.put("methodCount", stats.size());
        
        // 计算总体统计
        long totalCalls = stats.values().stream().mapToLong(PerformanceAspect.MethodPerformance::getCallCount).sum();
        long totalExecutionTime = stats.values().stream().mapToLong(PerformanceAspect.MethodPerformance::getTotalExecutionTime).sum();
        long totalSuccessCount = stats.values().stream().mapToLong(PerformanceAspect.MethodPerformance::getSuccessCount).sum();
        
        result.put("totalCalls", totalCalls);
        result.put("totalExecutionTime", totalExecutionTime);
        result.put("averageExecutionTime", totalCalls > 0 ? (double) totalExecutionTime / totalCalls : 0);
        result.put("overallSuccessRate", totalCalls > 0 ? (double) totalSuccessCount / totalCalls * 100 : 0);
        
        // 最慢的方法
        var slowestMethod = stats.values().stream()
            .max((p1, p2) -> Double.compare(p1.getAverageExecutionTime(), p2.getAverageExecutionTime()))
            .orElse(null);
        
        if (slowestMethod != null) {
            Map<String, Object> slowest = new HashMap<>();
            slowest.put("methodName", slowestMethod.getMethodName());
            slowest.put("layer", slowestMethod.getLayer());
            slowest.put("averageExecutionTime", slowestMethod.getAverageExecutionTime());
            result.put("slowestMethod", slowest);
        }
        
        // 调用次数最多的方法
        var mostCalledMethod = stats.values().stream()
            .max((p1, p2) -> Long.compare(p1.getCallCount(), p2.getCallCount()))
            .orElse(null);
        
        if (mostCalledMethod != null) {
            Map<String, Object> mostCalled = new HashMap<>();
            mostCalled.put("methodName", mostCalledMethod.getMethodName());
            mostCalled.put("layer", mostCalledMethod.getLayer());
            mostCalled.put("callCount", mostCalledMethod.getCallCount());
            result.put("mostCalledMethod", mostCalled);
        }
        
        return ResponseEntity.ok(result);
    }
}
