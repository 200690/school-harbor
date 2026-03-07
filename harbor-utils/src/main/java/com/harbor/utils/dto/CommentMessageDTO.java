package com.harbor.utils.dto;

import lombok.Data;

@Data
public class CommentMessageDTO {
    private Long toUserId;
    private String message;
}
