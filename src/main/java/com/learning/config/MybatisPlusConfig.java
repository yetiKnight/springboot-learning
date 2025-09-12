package com.learning.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis Plus配置类
 * 
 * 面试重点知识点：
 * 1. MyBatis Plus分页插件配置
 * 2. 自动填充处理器配置
 * 3. 数据库类型配置
 * 4. 拦截器链配置
 * 
 * @author 学习笔记
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {

    /**
     * MyBatis Plus拦截器配置
     * 面试重点：分页插件、性能分析插件等
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("配置MyBatis Plus拦截器");
        
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.H2);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(1000L);
        // 设置溢出总页数后是否进行处理
        paginationInterceptor.setOverflow(false);
        
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        log.info("MyBatis Plus拦截器配置完成");
        return interceptor;
    }

    /**
     * 自动填充处理器
     * 面试重点：审计字段自动填充、创建时间、更新时间
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        log.info("配置MyBatis Plus自动填充处理器");
        
        return new MetaObjectHandler() {
            
            /**
             * 插入时的填充策略
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                log.debug("执行插入填充策略");
                
                // 自动填充创建时间
                this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
                // 自动填充更新时间
                this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }

            /**
             * 更新时的填充策略
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                log.debug("执行更新填充策略");
                
                // 自动填充更新时间
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
