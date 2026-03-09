package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_message")
public class MessagePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long itemId;
    private String content;
    private Integer status; // 0: 未读, 1: 已读
    private LocalDateTime sendTime;
    private LocalDateTime readTime;
    private Integer isDelete; // 0: 正常, 1: 已删除
}
