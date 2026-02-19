package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewVO {
    private Long id;
    private Long orderId;
    private Long fromUserId;
    private String fromUserName;          // 评价人昵称
    private String fromUserAvatar;
    private Long toUserId;
    private String toUserName;            // 被评价人昵称
    private Integer rating;
    private String content;
    private LocalDateTime createTime;
}