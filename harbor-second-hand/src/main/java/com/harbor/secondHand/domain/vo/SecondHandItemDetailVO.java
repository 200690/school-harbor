package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SecondHandItemDetailVO {
    private Long id;
    private String title;
    private String description;
    private Integer categoryId;
    private String categoryName;          // 分类名称
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer condition;
    private String conditionDesc;
    private String coverImage;
    private List<String> images;           // 图片列表
    private Long sellerId;
    private String sellerName;
    private String sellerAvatar;
    private String sellerPhone;            // 卖家联系方式（可能需要权限）
    private String school;
    private String location;
    private Integer viewCount;
    private Integer favoriteCount;
    private Integer status;
    private LocalDateTime publishTime;
    private Boolean isFavorite;             // 当前用户是否收藏
    private Boolean isOwner;                 // 当前用户是否是卖家
}