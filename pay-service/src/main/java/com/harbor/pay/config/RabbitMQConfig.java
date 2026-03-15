package com.harbor.pay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String EXCHANGE_NAME = "harbor.exchange";

    // 队列名称
    public static final String ORDER_QUEUE_NAME = "harbor.order.queue";

    // 路由键
    public static final String USER_ROUTING_KEY = "harbor.pay";
    public static final String ORDER_ROUTING_KEY = "harbor.order";

    // 创建交换机
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 创建订单消息队列
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE_NAME)
                .build();
    }

    // 绑定订单消息队列到交换机
    @Bean
    public Binding orderBinding(Queue orderQueue, Exchange exchange) {
        return BindingBuilder.bind(orderQueue)
                .to(exchange)
                .with(ORDER_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 将时间序列化为 ISO-8601 字符串（如 "2026-03-03T21:59:48.6573618"）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
