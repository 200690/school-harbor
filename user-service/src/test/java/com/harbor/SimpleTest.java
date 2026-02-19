package com.harbor;

import com.harbor.secondHand.user.UserServiceApplication;
import com.harbor.secondHand.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserServiceApplication.class)  // 指定使用 user-service 的启动类
public class SimpleTest {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Test
    void test() {
        System.out.println("=== 测试开始 ===");
        System.out.println("UserMapper is null? " + (userMapper == null));
        System.out.println("MyBatis Plus 应该能正常工作");
        System.out.println("=== 测试结束 ===");
    }
}