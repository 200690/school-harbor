package com.harbor.secondHand.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageListVO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String avatar;
    private Long receiverId;
    private Long itemId;
    private String itemTitle;
    private String itemCoverImage;
    private String itemPrice;
    private Long otherUserId;
    private String otherUserNickname;
    private String otherUserAvatar;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
    private Integer status;
    private LocalDateTime createTime;
}