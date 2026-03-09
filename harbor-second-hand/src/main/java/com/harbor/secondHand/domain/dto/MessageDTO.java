package com.harbor.secondHand.domain.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Long receiverId;
    private Long itemId;
    private String content;
}
