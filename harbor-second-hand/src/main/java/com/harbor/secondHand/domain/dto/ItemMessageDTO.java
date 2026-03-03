package com.harbor.secondHand.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemMessageDTO {
    private Long itemId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime updateTime;
    private String operationType;
}
