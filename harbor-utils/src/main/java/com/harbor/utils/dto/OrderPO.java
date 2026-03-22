package com.harbor.utils.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("second_hand_order")
public class OrderPO {
    @TableId(type = IdType.AUTO)
    private Long id;                        // 订单ID
    private String orderNo;                  // 订单号
    private Long itemId;                     // 物品ID
    private Long sellerId;                   // 卖家ID
    private Long buyerId;                    // 买家ID
    private BigDecimal price;                 // 成交价格
    private Integer quantity;                 // 购买数量
    private BigDecimal totalAmount;           // 总金额
    private Integer status;                   // 订单状态（1:待付款, 2:待发货, 3:待收货, 0:已完成, 4:已取消, 5:退款中）
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderTime;          // 下单时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime payTime;            // 付款时间
    private Integer payStatus;                 // 支付状态：0-未支付，1-支付成功，2-支付失败，3-退款中，4-已退款
    private LocalDateTime deliveryTime;        // 发货时间
    private LocalDateTime receiveTime;         // 收货时间
    private LocalDateTime completeTime;        // 完成时间
    private LocalDateTime cancelTime;          // 取消时间
    private String cancelReason;               // 取消原因
    @TableLogic
    private Integer isDelete;                  // 是否删除
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;           // 更新时间
}