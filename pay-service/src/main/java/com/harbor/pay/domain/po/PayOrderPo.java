package com.harbor.pay.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单表 持久化对象
 */
@Data
@TableName("pay_order")
@Accessors(chain = true)
public class PayOrderPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 支付订单号 */
    private String payOrderNo;

    /** 业务订单号 */
    private String bizOrderNo;

    /** 业务订单ID */
    private Long bizOrderId;

    /** 买家ID */
    private Long buyerId;

    /** 卖家ID */
    private Long sellerId;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 优惠金额 */
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /** 订单状态(1-交易中。0-交易完成，2-取消交易） */
    private Integer status = 0;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Integer version = 1;
}