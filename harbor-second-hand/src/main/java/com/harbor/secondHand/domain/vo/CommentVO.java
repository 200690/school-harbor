package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    private Long id;
    private Long itemId;
    private Long fromUserId;
    private String fromUserName;          // 发送方昵称
    private String fromUserAvatar;        // 发送方头像
    private Long toUserId;
    private String toUserName;            // 接收方昵称
    private String content;
    private Long parentId;
    private LocalDateTime createTime;
    private List<CommentVO> replies;       // 回复列表
}