package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserBlockProductItemVO {
    /**
     * 拉黑记录ID
     */
    private Long blockId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productImage;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 状态
     */
    private Integer stats;

    /**
     * 状态描述
     */
    public String getStatsDesc() {
        return stats == 1 ? "生效中" : "已取消";
    }

    /**
     * 拉黑时间
     */
    private LocalDateTime blockTime;
}
