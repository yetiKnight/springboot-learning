package com.learning.analysis;

import com.learning.SpringbootLearningApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 准备上下文环节的源和Bean演示
 * 
 * 这个类演示了prepareContext阶段获取的源和加载的Bean
 * 
 * @author 学习笔记
 */
public class PrepareContextDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 准备上下文环节的源和Bean演示 ===");
        
        // 创建SpringApplication实例
        SpringApplication app = new SpringApplication(SpringbootLearningApplication.class);
        
        // 演示获取的源
        System.out.println("\n【1. 获取的源（Sources）】");
        System.out.println("主应用类: " + SpringbootLearningApplication.class.getName());
        System.out.println("主应用类注解: " + 
            java.util.Arrays.toString(SpringbootLearningApplication.class.getAnnotations()));
        
        // 运行应用（这会触发prepareContext）
        System.out.println("\n【2. 开始启动应用...】");
        ConfigurableApplicationContext context = app.run(args);
        
        // 演示加载的Bean
        System.out.println("\n【3. 加载的Bean定义】");
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("总Bean数量: " + beanNames.length);
        
        // 查找主应用类相关的Bean
        System.out.println("\n【4. 主应用类相关的Bean】");
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("springbootlearning")) {
                Object bean = context.getBean(beanName);
                System.out.println("Bean名称: " + beanName);
                System.out.println("Bean类型: " + bean.getClass().getName());
                System.out.println("Bean注解: " + 
                    java.util.Arrays.toString(bean.getClass().getAnnotations()));
            }
        }
        
        // 查找配置类相关的Bean
        System.out.println("\n【5. 配置类相关的Bean】");
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            if (bean.getClass().isAnnotationPresent(Configuration.class)) {
                System.out.println("配置类Bean: " + beanName + " -> " + bean.getClass().getName());
            }
        }
        
        // 查找组件相关的Bean
        System.out.println("\n【6. 组件相关的Bean】");
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            if (bean.getClass().isAnnotationPresent(Component.class)) {
                System.out.println("组件Bean: " + beanName + " -> " + bean.getClass().getName());
            }
        }
        
        System.out.println("\n【7. 关闭应用】");
        context.close();
    }
    
    /**
     * 演示配置类
     */
    @Configuration
    public static class DemoConfig {
        
        @Bean
        public String demoBean() {
            return "这是一个演示Bean";
        }
    }
    
    /**
     * 演示组件
     */
    @Component
    public static class DemoComponent {
        public String getMessage() {
            return "这是一个演示组件";
        }
    }
}
