package com.learning.controller;

import com.learning.service.ScheduledService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务控制器
 * 
 * 面试重点知识点：
 * 1. 定时任务的管理和监控
 * 2. 定时任务的启停控制
 * 3. 定时任务的状态查询
 * 4. 定时任务的配置管理
 * 5. 定时任务的异常处理
 * 
 * @author 学习笔记
 */
@RestController
@RequestMapping("/scheduled")
@RequiredArgsConstructor
@Slf4j
public class ScheduledController {

    private final ScheduledService scheduledService;

    /**
     * 获取定时任务状态
     * 面试重点：定时任务监控
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getScheduledStatus() {
        log.info("获取定时任务状态");
        
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        status.put("service", "springboot-learning");
        
        // 定时任务列表
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("fixedDelayTask", "每5秒执行一次，固定延迟");
        tasks.put("fixedRateTask", "每3秒执行一次，固定频率");
        tasks.put("initialDelayTask", "延迟10秒后开始，每2秒执行一次");
        tasks.put("cronTask", "每分钟执行一次");
        tasks.put("dailyTask", "每天上午10点执行");
        tasks.put("workdayTask", "周一到周五上午9点执行");
        tasks.put("monthEndTask", "每月最后一天执行");
        tasks.put("exceptionTask", "每10秒执行一次，包含异常处理");
        tasks.put("longRunningTask", "每30秒执行一次，长时间运行");
        tasks.put("conditionalTask", "每15秒执行一次，条件执行");
        
        status.put("tasks", tasks);
        status.put("totalTasks", tasks.size());
        
        return ResponseEntity.ok(status);
    }

    /**
     * 手动触发定时任务
     * 面试重点：定时任务的手动触发
     */
    @PostMapping("/trigger/{taskName}")
    public ResponseEntity<Map<String, Object>> triggerTask(@PathVariable String taskName) {
        log.info("手动触发定时任务：{}", taskName);
        
        Map<String, Object> result = new HashMap<>();
        result.put("taskName", taskName);
        result.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        try {
            // 根据任务名称手动触发
            switch (taskName) {
                case "fixedDelayTask":
                    scheduledService.fixedDelayTask();
                    result.put("status", "success");
                    result.put("message", "固定延迟任务已触发");
                    break;
                case "fixedRateTask":
                    scheduledService.fixedRateTask();
                    result.put("status", "success");
                    result.put("message", "固定频率任务已触发");
                    break;
                case "cronTask":
                    scheduledService.cronTask();
                    result.put("status", "success");
                    result.put("message", "Cron任务已触发");
                    break;
                case "dailyTask":
                    scheduledService.dailyTask();
                    result.put("status", "success");
                    result.put("message", "每日任务已触发");
                    break;
                case "workdayTask":
                    scheduledService.workdayTask();
                    result.put("status", "success");
                    result.put("message", "工作日任务已触发");
                    break;
                case "monthEndTask":
                    scheduledService.monthEndTask();
                    result.put("status", "success");
                    result.put("message", "月末任务已触发");
                    break;
                case "exceptionTask":
                    scheduledService.exceptionTask();
                    result.put("status", "success");
                    result.put("message", "异常处理任务已触发");
                    break;
                case "longRunningTask":
                    scheduledService.longRunningTask();
                    result.put("status", "success");
                    result.put("message", "长时间运行任务已触发");
                    break;
                case "conditionalTask":
                    scheduledService.conditionalTask();
                    result.put("status", "success");
                    result.put("message", "条件执行任务已触发");
                    break;
                default:
                    result.put("status", "error");
                    result.put("message", "未知的任务名称");
                    return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            log.error("触发定时任务失败：{}", taskName, e);
            result.put("status", "error");
            result.put("message", "触发任务失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取定时任务配置
     * 面试重点：定时任务配置管理
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getScheduledConfig() {
        log.info("获取定时任务配置");
        
        Map<String, Object> config = new HashMap<>();
        config.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 定时任务配置
        Map<String, Object> taskConfigs = new HashMap<>();
        
        Map<String, Object> fixedDelayConfig = new HashMap<>();
        fixedDelayConfig.put("type", "fixedDelay");
        fixedDelayConfig.put("value", "5000");
        fixedDelayConfig.put("description", "每5秒执行一次，固定延迟");
        taskConfigs.put("fixedDelayTask", fixedDelayConfig);
        
        Map<String, Object> fixedRateConfig = new HashMap<>();
        fixedRateConfig.put("type", "fixedRate");
        fixedRateConfig.put("value", "3000");
        fixedRateConfig.put("description", "每3秒执行一次，固定频率");
        taskConfigs.put("fixedRateTask", fixedRateConfig);
        
        Map<String, Object> initialDelayConfig = new HashMap<>();
        initialDelayConfig.put("type", "initialDelay + fixedRate");
        initialDelayConfig.put("initialDelay", "10000");
        initialDelayConfig.put("fixedRate", "2000");
        initialDelayConfig.put("description", "延迟10秒后开始，每2秒执行一次");
        taskConfigs.put("initialDelayTask", initialDelayConfig);
        
        Map<String, Object> cronConfig = new HashMap<>();
        cronConfig.put("type", "cron");
        cronConfig.put("value", "0 * * * * ?");
        cronConfig.put("description", "每分钟执行一次");
        taskConfigs.put("cronTask", cronConfig);
        
        Map<String, Object> dailyConfig = new HashMap<>();
        dailyConfig.put("type", "cron");
        dailyConfig.put("value", "0 0 10 * * ?");
        dailyConfig.put("description", "每天上午10点执行");
        taskConfigs.put("dailyTask", dailyConfig);
        
        Map<String, Object> workdayConfig = new HashMap<>();
        workdayConfig.put("type", "cron");
        workdayConfig.put("value", "0 0 9 * * MON-FRI");
        workdayConfig.put("description", "周一到周五上午9点执行");
        taskConfigs.put("workdayTask", workdayConfig);
        
        Map<String, Object> monthEndConfig = new HashMap<>();
        monthEndConfig.put("type", "cron");
        monthEndConfig.put("value", "0 0 0 L * ?");
        monthEndConfig.put("description", "每月最后一天执行");
        taskConfigs.put("monthEndTask", monthEndConfig);
        
        config.put("taskConfigs", taskConfigs);
        config.put("totalConfigs", taskConfigs.size());
        
        return ResponseEntity.ok(config);
    }

    /**
     * 获取定时任务统计信息
     * 面试重点：定时任务统计和监控
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getScheduledStatistics() {
        log.info("获取定时任务统计信息");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // 模拟统计信息
        Map<String, Object> taskStats = new HashMap<>();
        taskStats.put("totalTasks", 10);
        taskStats.put("runningTasks", 8);
        taskStats.put("stoppedTasks", 2);
        taskStats.put("failedTasks", 0);
        taskStats.put("successRate", "95.5%");
        
        statistics.put("taskStatistics", taskStats);
        
        // 执行时间统计
        Map<String, Object> executionStats = new HashMap<>();
        executionStats.put("averageExecutionTime", "1.2s");
        executionStats.put("maxExecutionTime", "5.0s");
        executionStats.put("minExecutionTime", "0.1s");
        executionStats.put("totalExecutions", 1250);
        
        statistics.put("executionStatistics", executionStats);
        
        return ResponseEntity.ok(statistics);
    }
}
