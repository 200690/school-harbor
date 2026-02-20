package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
    //卖家信息
@Data
public class SellerInfoVO {
    private Long id;
    private String name;
    private String avatar;
    private String phone;                    // 联系方式
    private Integer creditScore;              // 信誉分
    private Long itemCount;                   // 在售商品数量
    private Long soldCount;                   // 已售商品数量
    private Double positiveRate;               // 好评率
}