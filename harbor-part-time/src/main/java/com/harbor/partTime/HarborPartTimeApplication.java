package com.harbor.partTime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.harbor.partTime.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.harbor.utils.client")
public class HarborPartTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarborPartTimeApplication.class, args);
    }

}
