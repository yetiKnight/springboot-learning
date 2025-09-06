package com.learning.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：记录方法执行时间
 * 
 * 面试重点知识点：
 * 1. 自定义注解的定义和使用
 * 2. 元注解的作用：@Target、@Retention
 * 3. 注解与AOP的结合使用
 * 4. 注解处理器的工作原理
 * 
 * @author 学习笔记
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    
    /**
     * 是否记录参数
     */
    boolean logArgs() default false;
    
    /**
     * 是否记录返回值
     */
    boolean logResult() default false;
    
    /**
     * 日志级别
     */
    String level() default "INFO";
}
