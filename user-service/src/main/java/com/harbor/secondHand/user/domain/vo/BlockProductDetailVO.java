package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 完整拉黑信息视图对象（包含用户信息）
 */
@Data
@Accessors(chain = true)
public class BlockProductDetailVO {
    /**
     * 拉黑记录ID
     * 拉黑表记录
     */
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime blockTime;

    // ========== 被拉黑商品信息 ==========

    /**
     * 商品名称
     */
    private String title;

    /**
     * 商品图片
     */
    private String coverImage;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品描述
     */
    private String description;
}