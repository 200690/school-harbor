package com.harbor.secondHand.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BrowseHistoryVO {
    private Long id;
    private Long userId;
    private Long itemId;
    private LocalDateTime browseTime;
    // 关联的商品信息
    private String itemTitle;
    private BigDecimal itemPrice;
    private String itemCoverImage;
    private String itemSchool;
}