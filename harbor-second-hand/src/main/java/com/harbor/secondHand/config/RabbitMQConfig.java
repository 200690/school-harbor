package com.harbor.secondHand.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String EXCHANGE_NAME = "harbor.exchange";
    
    // 路由键
    public static final String ITEM_ROUTING_KEY = "harbor.item";

    // 创建交换机
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }
}
