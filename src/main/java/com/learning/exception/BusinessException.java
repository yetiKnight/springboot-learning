package com.learning.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常类
 * 
 * 面试重点知识点：
 * 1. 自定义异常的设计原则
 * 2. 异常继承体系的设计
 * 3. 异常信息的封装
 * 4. HTTP状态码的映射
 * 
 * @author 学习笔记
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String error;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.error = "业务异常";
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.error = "业务异常";
    }

    public BusinessException(String message, String error, HttpStatus status) {
        super(message);
        this.status = status;
        this.error = error;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
        this.error = "业务异常";
    }
}
