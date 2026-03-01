package com.harbor.messageservice.domain.vo;

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
public class MessageProcessedVO {
    private String messageId;
    private String handler;
    private LocalDateTime processedTime; // 前端可能需要显示处理时间
}