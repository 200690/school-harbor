package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    private Long id;
    private String orderNo;

    // 商品信息
    private Long itemId;
    private String itemTitle;
    private String itemDescription;
    private List<String> itemImages;      // 商品图片列表
    private String itemCoverImage;

    // 交易信息
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String tradeMethod;
    private String tradeLocation;
    private String deliveryNo;

    // 状态信息
    private Integer status;
    private String statusDesc;
    private LocalDateTime orderTime;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;
    private LocalDateTime completeTime;
    private LocalDateTime cancelTime;
    private String cancelReason;

    // 支付信息
    private Integer payStatus;
    private String payStatusDesc;
    private String payOrderNo;
    private String payChannel;

    // 用户信息
    private Long sellerId;
    private String sellerName;
    private String sellerPhone;
    private Long buyerId;
    private String buyerName;
    private String buyerPhone;

    // 留言备注
    private String buyerMessage;
    private String sellerRemark;

    // 操作权限
    private Boolean canPay;               // 能否付款
    private Boolean canCancel;            // 能否取消
    private Boolean canDeliver;           // 能否发货
    private Boolean canReceive;           // 能否收货
    private Boolean canReview;            // 能否评价
}