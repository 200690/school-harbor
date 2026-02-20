package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("second_hand_item")
public class ItemPO {
    @TableId(type = IdType.AUTO)
    private Long id;                    // 商品ID
    private String title;                // 商品标题
    private String description;          // 商品描述
    private Integer categoryId;          // 分类ID
    private BigDecimal price;            // 价格
    private BigDecimal originalPrice;    // 原价/参考价
    @TableField("`condition`")
    private Integer condition;           // 成色：1-全新，2-99新，3-95新，4-9成新，5-8成新，6-7成新及以下
    private String coverImage;           // 封面图片URL
    private String images;               // 图片列表（JSON数组或逗号分隔）
    private Long sellerId;               // 卖家ID
    private String school;                // 所在学校
    private String location;              // 具体位置
    private Integer viewCount;            // 浏览次数
    private Integer favoriteCount;        // 收藏次数
    private Integer status;               // 状态：0-下架，1-上架，2-已售出，3-审核中，4-审核不通过
    private String auditRemark;           // 审核备注
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime publishTime;    // 发布时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;     // 更新时间
    @TableLogic
    private Integer isDelete;             // 是否删除
}