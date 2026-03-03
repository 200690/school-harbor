package com.harbor.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public ChatWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 用户登录时绑定会话
    public void bindUserSession(String userId, String sessionId) {
        userSessionMap.put(userId, sessionId);
    }

    // 发送消息给指定用户
    public void sendNotification(String userId, String message) {
        String sessionId = userSessionMap.get(userId);
        if (sessionId != null) {
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/notifications",
                    message
            );
        } else {
            // 用户不在线，存储离线消息（可选）
            saveOfflineMessage(userId, message);
        }
    }

    private void saveOfflineMessage(String userId, String message) {
        // 保存到数据库或Redis
        // 后续登录时推送
    }
}