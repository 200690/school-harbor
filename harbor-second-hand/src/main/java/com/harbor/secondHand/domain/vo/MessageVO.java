package com.harbor.secondHand.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    private Long receiverId;
    private Long itemId;
    private String itemTitle;
    private String content;
    private Integer status;
    private LocalDateTime sendTime;
    private LocalDateTime readTime;
}