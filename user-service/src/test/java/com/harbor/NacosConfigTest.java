package com.harbor;

import com.harbor.user.UserServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(classes = UserServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class NacosConfigTest {

    @Autowired
    private Environment env;

    @Test
    void testConfigLoading() {
        System.out.println("===== 配置加载测试 =====");
        System.out.println("数据库URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("数据库用户名: " + env.getProperty("spring.datasource.username"));
        System.out.println("MyBatis Mapper位置: " + env.getProperty("mybatis-plus.mapper-locations"));
        System.out.println("=====================");

        // 如果这些值为 null，说明 Nacos 配置没加载
    }
}