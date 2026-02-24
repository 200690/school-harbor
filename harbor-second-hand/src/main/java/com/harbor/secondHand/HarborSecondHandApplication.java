package com.harbor.secondHand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.harbor.secondHand.mapper")
@EnableFeignClients(basePackages = "com.harbor.utils.client")
public class HarborSecondHandApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarborSecondHandApplication.class, args);
    }

}
