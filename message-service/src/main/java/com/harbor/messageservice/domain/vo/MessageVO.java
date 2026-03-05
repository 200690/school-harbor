package com.harbor.messageservice.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MessageVO {
    private String id;              // 消息唯一ID
    private String message;         // 消息通知内容
    private LocalDateTime processedTime; // 通知时间
    private Integer status;         // 状态(0:未处理 1:处理中 2:处理成功 3:处理失败)
}
