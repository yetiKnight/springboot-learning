package com.learning.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 自动配置测试类
 * 
 * 面试重点：通过测试验证自动配置的工作原理
 * 
 * @author 学习笔记
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class AutoConfigurationTest {

    /**
     * 测试数据源自动配置
     * 面试重点：验证自动配置是否生效
     */
    @Test
    public void testDataSourceAutoConfiguration(ApplicationContext context) {
        System.out.println("=== 测试数据源自动配置 ===");
        
        // 1. 检查数据源Bean是否存在
        assertThat(context.containsBean("dataSource")).isTrue();
        
        // 2. 获取数据源Bean
        DataSource dataSource = context.getBean(DataSource.class);
        assertThat(dataSource).isNotNull();
        
        // 3. 测试数据源连接
        try {
            Connection connection = dataSource.getConnection();
            assertThat(connection).isNotNull();
            System.out.println("数据源连接成功: " + connection.getMetaData().getURL());
            connection.close();
        } catch (SQLException e) {
            System.out.println("数据源连接失败: " + e.getMessage());
        }
        
        // 4. 检查相关Bean
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("总Bean数量: " + beanNames.length);
        
        // 5. 查找自动配置相关的Bean
        for (String beanName : beanNames) {
            if (beanName.contains("auto") || beanName.contains("Auto")) {
                System.out.println("自动配置Bean: " + beanName);
            }
        }
    }

    /**
     * 测试JPA自动配置
     * 面试重点：验证JPA相关Bean的自动配置
     */
    @Test
    public void testJpaAutoConfiguration(ApplicationContext context) {
        System.out.println("\n=== 测试JPA自动配置 ===");
        
        // 1. 检查实体管理器工厂
        assertThat(context.containsBean("entityManagerFactory")).isTrue();
        
        // 2. 检查事务管理器
        assertThat(context.containsBean("transactionManager")).isTrue();
        
        // 3. 检查JPA相关Bean
        String[] jpaBeans = {
            "entityManagerFactory",
            "transactionManager",
            "jpaVendorAdapter",
            "jpaDialect"
        };
        
        for (String beanName : jpaBeans) {
            if (context.containsBean(beanName)) {
                System.out.println("JPA Bean存在: " + beanName);
            } else {
                System.out.println("JPA Bean缺失: " + beanName);
            }
        }
    }

    /**
     * 测试Web自动配置
     * 面试重点：验证Web相关Bean的自动配置
     */
    @Test
    public void testWebAutoConfiguration(ApplicationContext context) {
        System.out.println("\n=== 测试Web自动配置 ===");
        
        // 1. 检查DispatcherServlet
        assertThat(context.containsBean("dispatcherServlet")).isTrue();
        
        // 2. 检查Web相关Bean
        String[] webBeans = {
            "dispatcherServlet",
            "requestMappingHandlerMapping",
            "requestMappingHandlerAdapter",
            "viewResolver"
        };
        
        for (String beanName : webBeans) {
            if (context.containsBean(beanName)) {
                System.out.println("Web Bean存在: " + beanName);
            } else {
                System.out.println("Web Bean缺失: " + beanName);
            }
        }
    }

    /**
     * 测试条件注解
     * 面试重点：验证条件注解的工作原理
     */
    @Test
    public void testConditionalAnnotations(ApplicationContext context) {
        System.out.println("\n=== 测试条件注解 ===");
        
        // 1. 检查类路径条件
        boolean dataSourceClassExists = checkClassExists("javax.sql.DataSource");
        System.out.println("DataSource类存在: " + dataSourceClassExists);
        
        // 2. 检查Bean缺失条件
        boolean dataSourceBeanMissing = !context.containsBean("dataSource");
        System.out.println("DataSource Bean缺失: " + dataSourceBeanMissing);
        
        // 3. 检查配置属性条件
        boolean dataSourcePropertyExists = checkPropertyExists("spring.datasource.url");
        System.out.println("数据源配置属性存在: " + dataSourcePropertyExists);
    }

    /**
     * 测试自动配置报告
     * 面试重点：理解自动配置报告的作用
     */
    @Test
    public void testAutoConfigurationReport(ApplicationContext context) {
        System.out.println("\n=== 测试自动配置报告 ===");
        
        // 1. 获取所有Bean定义
        String[] beanNames = context.getBeanDefinitionNames();
        
        // 2. 统计自动配置Bean
        int autoConfigBeans = 0;
        for (String beanName : beanNames) {
            if (beanName.contains("auto") || beanName.contains("Auto")) {
                autoConfigBeans++;
            }
        }
        
        System.out.println("自动配置Bean数量: " + autoConfigBeans);
        System.out.println("总Bean数量: " + beanNames.length);
        System.out.println("自动配置比例: " + (double) autoConfigBeans / beanNames.length * 100 + "%");
    }

    /**
     * 检查类是否存在
     */
    private boolean checkClassExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 检查配置属性是否存在
     */
    private boolean checkPropertyExists(String property) {
        // 模拟检查配置属性
        return true; // 假设存在
    }
}
