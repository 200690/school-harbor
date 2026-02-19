package com.harbor;

import com.harbor.secondHand.user.UserServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("local")  // 指定使用 local 配置文件
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("数据库连接成功：" + conn.getCatalog());
            System.out.println("数据库URL: " + conn.getMetaData().getURL());
        }
    }
}