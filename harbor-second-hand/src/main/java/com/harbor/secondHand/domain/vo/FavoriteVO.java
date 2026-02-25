package com.harbor.secondHand.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FavoriteVO {
    private Long id;
    private Long userId;
    private Long itemId;
    private LocalDateTime createTime;
    // 关联的商品信息
    private String itemTitle;
    private BigDecimal itemPrice;
    private String itemCoverImage;
    private String itemSchool;
    private String itemLocation;
}