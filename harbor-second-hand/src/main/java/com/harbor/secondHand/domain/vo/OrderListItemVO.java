package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListItemVO {
    private Long id;
    private String orderNo;
    private Long itemId;
    private String itemTitle;                   // 商品标题
    private String itemCoverImage;              // 商品封面图
    private BigDecimal price;             // 成交单价
    private Integer quantity;             // 购买数量
    private BigDecimal totalAmount;       // 总金额
    private String tradeMethod;           // 交易方式
    private String tradeLocation;         // 交易地点
    private Integer status;               // 订单状态
    private String statusDesc;            // 订单状态（1:待付款, 2:待发货, 3:待收货, 0:已完成, 4:已取消, 5:退款中）
    private LocalDateTime orderTime;      // 下单时间
    private Long sellerId;
    private Long buyerId;
    private String buyerName;             // 买家昵称
    private Integer payStatus;            // 支付状态
}