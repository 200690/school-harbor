package com.harbor.messageservice.domain.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class MessageProcessedPO {
    private String messageId;        // 消息唯一ID
    private String handler;          // 处理器名称
    private LocalDateTime processedTime; // 处理时间
}