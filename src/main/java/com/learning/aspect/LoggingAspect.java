package com.learning.aspect;

import com.learning.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志切面
 * 
 * 面试重点知识点：
 * 1. AOP切面的实现原理
 * 2. @Aspect注解的作用
 * 3. 切点表达式的编写
 * 4. 通知方法的类型和参数
 * 5. 动态代理的实现机制
 * 
 * @author 学习笔记
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * 环绕通知：记录方法执行时间
     * 面试重点：ProceedingJoinPoint的使用
     */
    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        
        // 记录方法参数
        if (logExecutionTime.logArgs()) {
            Object[] args = joinPoint.getArgs();
            log.info("方法调用: {}.{}, 参数: {}", className, methodName, args);
        } else {
            log.info("方法调用: {}.{}", className, methodName);
        }
        
        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            log.error("方法执行异常: {}.{}, 异常: {}", className, methodName, e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // 记录执行时间
            if (logExecutionTime.logResult()) {
                log.info("方法执行完成: {}.{}, 执行时间: {}ms, 返回值: {}", 
                        className, methodName, executionTime, result);
            } else {
                log.info("方法执行完成: {}.{}, 执行时间: {}ms", 
                        className, methodName, executionTime);
            }
        }
    }
}
