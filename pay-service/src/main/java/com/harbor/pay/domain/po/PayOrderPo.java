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

    /** 支付订单号（唯一） */
    private String payOrderNo;

    /** 业务类型（second:二手, part-time:兼职） */
    private String bizType;

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

    /** 支付方式（1-微信, 2-支付宝, 3-余额） */
    private Integer payMethod;

    /** 支付状态（0:待支付, 1:支付中, 2:支付成功, 3:支付失败, 4:已退款） */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 第三方支付流水号 */
    private String transactionId;

    /** 退款状态（0:无退款, 1:退款中, 2:已退款） */
    private Integer refundStatus = 0;

    /** 退款金额 */
    private BigDecimal refundAmount = BigDecimal.ZERO;

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