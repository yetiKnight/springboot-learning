package com.learning.exception;

/**
 * 资源未找到异常
 * 
 * 面试重点知识点：
 * 1. 异常命名的规范
 * 2. 异常继承体系的设计
 * 3. 异常的使用场景
 * 
 * @author 学习笔记
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
