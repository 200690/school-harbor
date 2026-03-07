package com.harbor.messageservice.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageProcessedDTO {
    private String id;
    private String message;
    private LocalDateTime processedTime;
    private Long userId;
    private Integer status;
}
