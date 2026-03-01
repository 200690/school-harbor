package com.harbor.pay.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单 视图对象
 */
@Data
@Accessors(chain = true)
public class PayOrderVo {

    private Long id;

    /** 支付订单号 */
    private String payOrderNo;

    /** 业务类型 */
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
    private BigDecimal discountAmount;

    /** 支付方式（1-微信, 2-支付宝, 3-余额） */
    private Integer payMethod;

    /** 支付方式描述 */
    public String getPayMethodDesc() {
        if (payMethod == null) return "";
        return switch (payMethod) {
            case 1 -> "微信";
            case 2 -> "支付宝";
            case 3 -> "余额";
            default -> "未知";
        };
    }

    /** 支付状态（0:待支付, 1:支付中, 2:支付成功, 3:支付失败, 4:已退款） */
    private Integer payStatus;

    /** 支付状态描述 */
    public String getPayStatusDesc() {
        if (payStatus == null) return "";
        return switch (payStatus) {
            case 0 -> "待支付";
            case 1 -> "支付中";
            case 2 -> "支付成功";
            case 3 -> "支付失败";
            case 4 -> "已退款";
            default -> "未知";
        };
    }

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /** 第三方支付流水号 */
    private String transactionId;

    /** 退款状态（0:无退款, 1:退款中, 2:已退款） */
    private Integer refundStatus;

    /** 退款状态描述 */
    public String getRefundStatusDesc() {
        if (refundStatus == null) return "";
        return switch (refundStatus) {
            case 0 -> "无退款";
            case 1 -> "退款中";
            case 2 -> "已退款";
            default -> "未知";
        };
    }

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}