package com.harbor.messageservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String EXCHANGE_NAME = "harbor.exchange";
    
    // 队列名称
    public static final String JOB_QUEUE_NAME = "harbor.job.queue";
    public static final String ITEM_QUEUE_NAME = "harbor.item.queue";
    public static final String USER_QUEUE_NAME = "harbor.user.queue";
    
    // 路由键
    public static final String JOB_ROUTING_KEY = "harbor.job";
    public static final String ITEM_ROUTING_KEY = "harbor.item";
    public static final String USER_ROUTING_KEY = "harbor.user";

    // 创建交换机
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 创建兼职信息队列
    @Bean
    public Queue jobQueue() {
        return QueueBuilder.durable(JOB_QUEUE_NAME)
                .build();
    }

    // 创建二手交易信息队列
    @Bean
    public Queue itemQueue() {
        return QueueBuilder.durable(ITEM_QUEUE_NAME)
                .build();
    }

    // 创建用户信息队列
    @Bean
    public Queue userQueue() {
        return QueueBuilder.durable(USER_QUEUE_NAME)
                .build();
    }

    // 绑定兼职信息队列到交换机
    @Bean
    public Binding jobBinding(Queue jobQueue, Exchange exchange) {
        return BindingBuilder.bind(jobQueue)
                .to(exchange)
                .with(JOB_ROUTING_KEY)
                .noargs();
    }

    // 绑定二手交易信息队列到交换机
    @Bean
    public Binding itemBinding(Queue itemQueue, Exchange exchange) {
        return BindingBuilder.bind(itemQueue)
                .to(exchange)
                .with(ITEM_ROUTING_KEY)
                .noargs();
    }

    // 绑定用户信息队列到交换机
    @Bean
    public Binding userBinding(Queue userQueue, Exchange exchange) {
        return BindingBuilder.bind(userQueue)
                .to(exchange)
                .with(USER_ROUTING_KEY)
                .noargs();
    }
}
