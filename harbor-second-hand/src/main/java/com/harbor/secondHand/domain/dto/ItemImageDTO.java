package com.harbor.secondHand.domain.dto;

import lombok.Data;

@Data
public class ItemImageDTO {
    private Long id;
    private Long itemId;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isCover;          // 使用Boolean便于前端处理
}