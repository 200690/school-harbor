package com.harbor.partTime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.harbor.partTime.mapper")
@SpringBootApplication
public class HarborPartTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarborPartTimeApplication.class, args);
    }

}
