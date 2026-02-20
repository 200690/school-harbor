package com.harbor.secondHand.domain.vo;

import lombok.Data;

@Data
public class ItemImageVO {
    private Long id;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isCover;
}