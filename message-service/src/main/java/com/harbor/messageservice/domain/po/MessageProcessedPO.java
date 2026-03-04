package com.harbor.messageservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("message_processed")
public class MessageProcessedPO {
    @TableId(type = IdType.INPUT)
    private String id;        // 消息唯一ID
    private String message;          // 消息通知内容
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime processedTime; // 通知时间
    private Long userId;            // 消息通知接收者ID
    private Integer status;     // 状态(0:未处理 1:处理中 2:处理成功 3:处理失败)
}