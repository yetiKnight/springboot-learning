package com.learning.analysis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * SpringBoot启动组件测试
 * 
 * 本测试类用于验证启动组件的正确性
 * 
 * @author 学习笔记
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.application.name=startup-components-test",
    "logging.level.com.learning.analysis=DEBUG"
})
public class StartupComponentsTest {

    @Test
    public void testStartupComponents() {
        // 这个测试会启动SpringBoot应用，验证所有启动组件是否正常工作
        // 通过日志输出可以看到各个组件的执行顺序和时机
        System.out.println("✅ 启动组件测试完成");
    }
}
