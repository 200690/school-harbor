package com.harbor.secondHand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.harbor.secondHand.mapper")
public class HarborSecondHandApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarborSecondHandApplication.class, args);
    }

}
