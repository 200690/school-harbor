package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemListItemVO {
    // 商品基本信息
    private Long id;
    private String title;
    private BigDecimal price;
    private String coverImage;                 // 封面图片

    // 商品属性
    private Integer condition;
    private String conditionDesc;               // 成色描述（如"95新"）
    private String school;                      // 所在学校
    private String location;                     // 具体位置
    private Integer status;                     // 状态（0:已下架, 1:招聘中, 2:已招满, 3:已截止）

    // 卖家信息
    private Long sellerId;
    private String sellerName;                  // 卖家昵称
    private String sellerAvatar;                 // 卖家头像

    // 统计信息
    private Integer viewCount;
    private Integer favoriteCount;
    private LocalDateTime publishTime;

    // 交互状态
//    private Boolean isFavorite;                  // 当前用户是否收藏（需要传入当前用户ID）
}