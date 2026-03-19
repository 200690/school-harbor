package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("second_hand_item")
public class ItemPO {
    @TableId(type = IdType.AUTO)
    private Long id;                    // 商品ID
    @NotNull
    private String title;                // 商品标题
    @NotNull
    private String description;          // 商品描述
    @NotNull
    private Integer categoryId;          // 分类ID
    @NotNull
    private BigDecimal price;            // 价格
    private BigDecimal originalPrice;    // 原价/参考价
    @TableField("`condition`")
    @NotNull
    private Integer condition;           // 成色：1-全新，2-99新，3-95新，4-9成新，5-8成新，6-7成新及以下
    private String coverImage;           // 封面图片URL
    private String images;               // 图片列表（JSON数组或逗号分隔）
    @NotNull
    private Long sellerId;               // 卖家ID
    private String school;                // 所在学校
    private String location;              // 具体位置
    private Integer viewCount = 0;            // 浏览次数
    private Integer favoriteCount = 0;        // 收藏次数
    private Integer status;               // 状态：0-下架，1-上架，2-交易中，3-交易完成，4-交易取消
    private String auditRemark;           // 审核备注
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime publishTime;    // 发布时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;     // 更新时间
    @TableLogic
    private Integer isDelete = 0;             // 是否删除
}