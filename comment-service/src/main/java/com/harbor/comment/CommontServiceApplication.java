package com.harbor.comment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@MapperScan("com.harbor.comment.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.harbor.utils.client")
public class CommontServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommontServiceApplication.class, args);
	}

}
