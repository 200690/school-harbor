package com.harbor.secondHand.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单消息DTO
 * 用于消息队列传输订单信息
 */
@Data
public class OrderMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 操作类型：CREATE, UPDATE, CANCEL
     */
    private String operationType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
