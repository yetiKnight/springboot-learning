package com.learning.analysis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Bean生命周期演示
 * 
 * 面试重点：通过代码演示Bean的完整生命周期
 * 
 * @author 学习笔记
 */
public class BeanLifecycleDemo {

    public static void main(String[] args) {
        System.out.println("=== Bean生命周期演示 ===");
        SpringApplication.run(BeanLifecycleDemo.class, args);
    }
    
    /**
     * BeanFactoryPostProcessor演示
     * 面试重点：理解BeanFactoryPostProcessor的作用时机
     */
    @Component
    public static class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("=== 阶段1：BeanFactoryPostProcessor.postProcessBeanFactory() ===");
            System.out.println("BeanFactory类型: " + beanFactory.getClass().getSimpleName());
            System.out.println("Bean定义数量: " + beanFactory.getBeanDefinitionCount());
        }
    }
    
    /**
     * BeanPostProcessor演示
     * 面试重点：理解BeanPostProcessor的作用时机
     */
    @Component
    public static class CustomBeanPostProcessor implements BeanPostProcessor {
        
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof DemoBean) {
                System.out.println("=== 阶段2：BeanPostProcessor.postProcessBeforeInitialization() ===");
                System.out.println("Bean名称: " + beanName);
                System.out.println("Bean类型: " + bean.getClass().getSimpleName());
            }
            return bean;
        }
        
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof DemoBean) {
                System.out.println("=== 阶段3：BeanPostProcessor.postProcessAfterInitialization() ===");
                System.out.println("Bean名称: " + beanName);
                System.out.println("Bean类型: " + bean.getClass().getSimpleName());
            }
            return bean;
        }
    }
    
    /**
     * 演示Bean
     * 面试重点：理解Bean的完整生命周期
     */
    @Component
    public static class DemoBean implements BeanNameAware, InitializingBean, DisposableBean {
        
        private String beanName;
        
        public DemoBean() {
            System.out.println("=== 阶段4：构造函数调用 ===");
        }
        
        @Override
        public void setBeanName(String name) {
            System.out.println("=== 阶段5：BeanNameAware.setBeanName() ===");
            System.out.println("Bean名称: " + name);
            this.beanName = name;
        }
        
        @PostConstruct
        public void postConstruct() {
            System.out.println("=== 阶段6：@PostConstruct方法调用 ===");
        }
        
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("=== 阶段7：InitializingBean.afterPropertiesSet() ===");
        }
        
        @PreDestroy
        public void preDestroy() {
            System.out.println("=== 阶段8：@PreDestroy方法调用 ===");
        }
        
        @Override
        public void destroy() throws Exception {
            System.out.println("=== 阶段9：DisposableBean.destroy() ===");
        }
        
        public void doSomething() {
            System.out.println("=== Bean使用阶段 ===");
            System.out.println("执行业务逻辑");
        }
    }
}
