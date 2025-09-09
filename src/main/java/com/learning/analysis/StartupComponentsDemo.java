package com.learning.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * SpringBoot启动组件对比演示
 * 
 * 本类演示了SpringBoot启动过程中各种组件的执行时机和作用
 * 
 * @author 学习笔记
 */
@SpringBootApplication
public class StartupComponentsDemo {

    private static final Logger logger = LoggerFactory.getLogger(StartupComponentsDemo.class);

    public static void main(String[] args) {
        logger.info("🚀 === SpringBoot启动组件对比演示 ===");
        logger.info("📋 命令行参数: {}", Arrays.toString(args));
        logger.info("");

        // 创建SpringApplication实例
        SpringApplication app = new SpringApplication(StartupComponentsDemo.class);

        // 添加自定义初始化器
        app.addInitializers(new CustomApplicationContextInitializer());

        // 添加自定义监听器
        app.addListeners(new CustomApplicationListener());

        // 运行应用
        ConfigurableApplicationContext context = app.run(args);

        logger.info("🎉 === 应用启动完成 ===");
        logger.info("📊 Bean数量: {}", context.getBeanDefinitionCount());
        logger.info("🏗️ 应用类型: {}", context.getClass().getSimpleName());
        logger.info("");

        // 关闭应用
        logger.info("🔄 关闭应用...");
        context.close();
        logger.info("✅ 应用已关闭");
    }

    /**
     * 自定义ApplicationContextInitializer
     * 执行时机：在ApplicationContext刷新之前
     * 作用：初始化应用上下文配置
     */
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class CustomApplicationContextInitializer 
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final Logger logger = LoggerFactory.getLogger(CustomApplicationContextInitializer.class);

        @Override
        public void initialize(@org.springframework.lang.NonNull ConfigurableApplicationContext applicationContext) {
            logger.info("🔧 === ApplicationContextInitializer.initialize() ===");
            logger.info("  📍 执行时机：ApplicationContext刷新之前");
            logger.info("  🏗️ 上下文类型: {}", applicationContext.getClass().getSimpleName());
            logger.info("  🌍 环境信息: {}", applicationContext.getEnvironment().getClass().getSimpleName());
            logger.info("  📋 活跃Profile: {}", String.join(",", applicationContext.getEnvironment().getActiveProfiles()));
            
            // 设置环境属性
            applicationContext.getEnvironment().setActiveProfiles("dev");
            logger.info("  ✅ 设置活跃Profile为: dev");
            
            logger.info("  ✅ ApplicationContextInitializer执行完成");
            logger.info("");
        }
    }

    /**
     * 自定义ApplicationListener
     * 执行时机：当特定应用事件发生时
     * 作用：监听和处理应用生命周期事件
     */
    @Component
    public static class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

        private static final Logger logger = LoggerFactory.getLogger(CustomApplicationListener.class);

        @Override
        public void onApplicationEvent(@org.springframework.lang.NonNull ContextRefreshedEvent event) {
            logger.info("🎧 === ApplicationListener.onApplicationEvent() ===");
            logger.info("  📍 执行时机：ContextRefreshedEvent事件发生时");
            logger.info("  🏗️ 事件类型: {}", event.getClass().getSimpleName());
            logger.info("  🕐 时间戳: {}", new java.util.Date(event.getTimestamp()));
            
            // 获取应用上下文
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) event.getApplicationContext();
            
            // 执行启动后的逻辑
            logger.info("  📊 Bean数量: {}", context.getBeanDefinitionCount());
            logger.info("  🌍 活跃Profile: {}", Arrays.toString(context.getEnvironment().getActiveProfiles()));
            
            logger.info("  ✅ ApplicationListener执行完成");
            logger.info("");
        }
    }

    /**
     * 自定义ApplicationRunner
     * 执行时机：应用完全启动后
     * 作用：执行应用启动后的业务逻辑
     */
    @Component
    @Order(1)
    public static class CustomApplicationRunner implements ApplicationRunner {

        private static final Logger logger = LoggerFactory.getLogger(CustomApplicationRunner.class);

        @Override
        public void run(ApplicationArguments args) throws Exception {
            logger.info("🚀 === ApplicationRunner.run() ===");
            logger.info("  📍 执行时机：应用完全启动后");
            logger.info("  📋 命令行参数: {}", Arrays.toString(args.getSourceArgs()));
            logger.info("  🏷️ 选项参数: {}", args.getOptionNames());
            
            // 执行启动后的业务逻辑
            initializeData();
            startScheduledTasks();
            registerHealthChecks();
            
            logger.info("  ✅ ApplicationRunner执行完成");
            logger.info("");
        }

        private void initializeData() {
            logger.info("  📊 初始化数据...");
            // 模拟数据初始化
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 数据初始化完成");
        }

        private void startScheduledTasks() {
            logger.info("  ⏰ 启动定时任务...");
            // 模拟定时任务启动
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 定时任务启动完成");
        }

        private void registerHealthChecks() {
            logger.info("  🏥 注册健康检查...");
            // 模拟健康检查注册
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 健康检查注册完成");
        }
    }

    /**
     * 自定义CommandLineRunner
     * 执行时机：应用完全启动后
     * 作用：处理命令行参数，执行命令行相关的逻辑
     */
    @Component
    @Order(2)
    public static class CustomCommandLineRunner implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(CustomCommandLineRunner.class);

        @Override
        public void run(String... args) throws Exception {
            logger.info("💻 === CommandLineRunner.run() ===");
            logger.info("  📍 执行时机：应用完全启动后");
            logger.info("  📋 命令行参数: {}", Arrays.toString(args));
            
            // 解析命令行参数
            if (args.length > 0) {
                String command = args[0];
                switch (command) {
                    case "init-db":
                        initializeDatabase();
                        break;
                    case "migrate":
                        runMigrations();
                        break;
                    case "seed":
                        seedData();
                        break;
                    default:
                        logger.warn("  ⚠️ 未知命令: {}", command);
                }
            } else {
                logger.info("  ℹ️ 无命令行参数，执行默认逻辑");
                executeDefaultLogic();
            }
            
            logger.info("  ✅ CommandLineRunner执行完成");
            logger.info("");
        }

        private void initializeDatabase() {
            logger.info("  🗄️ 初始化数据库...");
            // 模拟数据库初始化
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 数据库初始化完成");
        }

        private void runMigrations() {
            logger.info("  🔄 运行数据库迁移...");
            // 模拟数据库迁移
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 数据库迁移完成");
        }

        private void seedData() {
            logger.info("  🌱 播种数据...");
            // 模拟数据播种
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 数据播种完成");
        }

        private void executeDefaultLogic() {
            logger.info("  🔧 执行默认逻辑...");
            // 模拟默认逻辑
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("  ✅ 默认逻辑执行完成");
        }
    }

    /**
     * 自定义BeanPostProcessor
     * 执行时机：在Bean实例化前后
     * 作用：处理Bean实例，如AOP代理、属性注入等
     */
    @Component
    public static class CustomBeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {

        private static final Logger logger = LoggerFactory.getLogger(CustomBeanPostProcessor.class);

        @Override
        public Object postProcessBeforeInitialization(@org.springframework.lang.NonNull Object bean, @org.springframework.lang.NonNull String beanName) throws org.springframework.beans.BeansException {
            if (bean.getClass().getPackage().getName().startsWith("com.learning")) {
                logger.info("🔧 === BeanPostProcessor.postProcessBeforeInitialization() ===");
                logger.info("  📍 执行时机：Bean初始化之前");
                logger.info("  🏷️ Bean名称: {}", beanName);
                logger.info("  🏗️ Bean类型: {}", bean.getClass().getSimpleName());
                logger.info("  ✅ BeanPostProcessor前置处理完成");
                logger.info("");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(@org.springframework.lang.NonNull Object bean, @org.springframework.lang.NonNull String beanName) throws org.springframework.beans.BeansException {
            if (bean.getClass().getPackage().getName().startsWith("com.learning")) {
                logger.info("🔧 === BeanPostProcessor.postProcessAfterInitialization() ===");
                logger.info("  📍 执行时机：Bean初始化之后");
                logger.info("  🏷️ Bean名称: {}", beanName);
                logger.info("  🏗️ Bean类型: {}", bean.getClass().getSimpleName());
                
                // 可以在这里创建代理对象
                if (bean.getClass().getSimpleName().contains("Service")) {
                    logger.info("  🎯 为Service类创建代理对象");
                    // 创建代理逻辑
                }
                
                logger.info("  ✅ BeanPostProcessor后置处理完成");
                logger.info("");
            }
            return bean;
        }
    }

    /**
     * 自定义BeanFactoryPostProcessor
     * 执行时机：在Bean定义加载后，Bean实例化前
     * 作用：修改Bean定义，如自动配置、属性占位符解析
     */
    @Component
    public static class CustomBeanFactoryPostProcessor implements org.springframework.beans.factory.config.BeanFactoryPostProcessor {

        private static final Logger logger = LoggerFactory.getLogger(CustomBeanFactoryPostProcessor.class);

        @Override
        public void postProcessBeanFactory(@org.springframework.lang.NonNull org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws org.springframework.beans.BeansException {
            logger.info("🏭 === BeanFactoryPostProcessor.postProcessBeanFactory() ===");
            logger.info("  📍 执行时机：Bean定义加载后，Bean实例化前");
            logger.info("  📊 Bean定义数量: {}", beanFactory.getBeanDefinitionCount());
            
            // 修改Bean定义
            String[] beanNames = beanFactory.getBeanDefinitionNames();
            int modifiedCount = 0;
            for (String beanName : beanNames) {
                org.springframework.beans.factory.config.BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                String beanClassName = beanDefinition.getBeanClassName();
                if (beanClassName != null && beanClassName.contains("Service")) {
                    
                    logger.info("  🔧 修改Service类Bean定义: {}", beanName);
                    // 修改Bean定义的属性
                    beanDefinition.setLazyInit(true);
                    modifiedCount++;
                }
            }
            
            logger.info("  📊 修改了 {} 个Bean定义", modifiedCount);
            logger.info("  ✅ BeanFactoryPostProcessor执行完成");
            logger.info("");
        }
    }

    /**
     * 测试服务类
     * 用于演示BeanPostProcessor的处理
     */
    @Component
    public static class TestService {
        
        private static final Logger logger = LoggerFactory.getLogger(TestService.class);
        
        public void doSomething() {
            logger.info("  🔧 TestService.doSomething() 被调用");
        }
    }
}
