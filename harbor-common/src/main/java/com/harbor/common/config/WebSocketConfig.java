package com.harbor.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnClass(EnableWebSocketMessageBroker.class)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用广播消息代理（用于推送通知）
        config.enableSimpleBroker("/topic");
        // 设置应用消息前缀（用于点对点通信）
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 添加WebSocket端点（支持SSE/长轮询降级）
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}