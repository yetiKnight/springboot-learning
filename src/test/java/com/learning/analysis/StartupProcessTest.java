package com.learning.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SpringBoot启动流程测试
 * 
 * 面试重点：通过测试验证启动流程的各个阶段
 * 
 * @author 学习笔记
 */
@SpringBootTest(classes = com.learning.SpringbootLearningApplication.class)
@TestPropertySource(properties = {
    "app.auto.enabled=true",
    "app.conditional.enabled=true"
})
public class StartupProcessTest {

    /**
     * 测试应用上下文加载
     * 面试重点：验证启动流程是否正常
     */
    @Test
    public void testApplicationContextLoads(ApplicationContext context) {
        System.out.println("=== 测试应用上下文加载 ===");
        
        // 验证应用上下文不为空
        assertThat(context).isNotNull();
        
        // 验证Bean数量
        int beanCount = context.getBeanDefinitionCount();
        System.out.println("Bean数量: " + beanCount);
        assertThat(beanCount).isGreaterThan(0);
        
        // 验证自定义服务是否加载
        if (context.containsBean("customService")) {
            System.out.println("✅ 自定义服务已加载");
            Object service = context.getBean("customService");
            assertThat(service).isNotNull();
        }
        
        // 验证条件服务是否加载
        if (context.containsBean("conditionalService")) {
            System.out.println("✅ 条件服务已加载");
            Object service = context.getBean("conditionalService");
            assertThat(service).isNotNull();
        }
    }
    
    /**
     * 测试Bean生命周期
     * 面试重点：验证Bean的创建过程
     */
    @Test
    public void testBeanLifecycle(ApplicationContext context) {
        System.out.println("=== 测试Bean生命周期 ===");
        
        // 验证DemoBean是否存在
        if (context.containsBean("demoBean")) {
            System.out.println("✅ DemoBean已创建");
            Object bean = context.getBean("demoBean");
            assertThat(bean).isNotNull();
        }
        
        // 验证BeanPostProcessor是否存在
        if (context.containsBean("customBeanPostProcessor")) {
            System.out.println("✅ BeanPostProcessor已注册");
        }
        
        // 验证BeanFactoryPostProcessor是否存在
        if (context.containsBean("customBeanFactoryPostProcessor")) {
            System.out.println("✅ BeanFactoryPostProcessor已注册");
        }
    }
    
    /**
     * 测试自动配置
     * 面试重点：验证自动配置是否生效
     */
    @Test
    public void testAutoConfiguration(ApplicationContext context) {
        System.out.println("=== 测试自动配置 ===");
        
        // 验证自动配置类是否加载
        String[] beanNames = context.getBeanDefinitionNames();
        boolean hasAutoConfig = false;
        for (String beanName : beanNames) {
            if (beanName.contains("Auto") || beanName.contains("auto")) {
                hasAutoConfig = true;
                System.out.println("自动配置Bean: " + beanName);
            }
        }
        
        if (hasAutoConfig) {
            System.out.println("✅ 自动配置已生效");
        } else {
            System.out.println("❌ 自动配置未生效");
        }
    }
}
