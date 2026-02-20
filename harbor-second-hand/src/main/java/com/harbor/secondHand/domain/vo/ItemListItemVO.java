package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemListItemVO {
    // 商品基本信息
    private Long id;
    private String title;
    private BigDecimal price;
    private String coverImage;                 // 封面图片
    private List<String> images;                // 图片列表（可选，列表页通常只展示封面）

    // 商品属性
    private Integer condition;
    private String conditionDesc;               // 成色描述（如"95新"）
    private String school;                      // 所在学校
    private String location;                     // 具体位置

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