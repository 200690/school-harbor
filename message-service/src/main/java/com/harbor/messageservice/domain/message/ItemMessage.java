package com.harbor.messageservice.domain.message;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemMessage {
    private Long itemId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime updateTime;
    private String operationType; // CREATE, UPDATE, DELETE
}
