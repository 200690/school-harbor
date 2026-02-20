package com.harbor.secondHand.domain.vo;

import com.harbor.common.utils.BeanUtils;
import com.harbor.secondHand.domain.po.ItemPO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDetailVO {
    // 商品基本信息
    private Long id;
    private String title;
    private String description;

    // 分类信息
    private Integer categoryId;

    // 价格信息
    private BigDecimal price;
    private BigDecimal originalPrice;

    // 商品属性
    private Integer condition;
    private String school;
    private String location;

    // 图片信息
    private String coverImage;
    private List<String> images;                  // 所有图片列表

    // 卖家信息
    private Long sellerId;
    private String sellerName = "卖家";
    private String sellerAvatar = "";                   // 卖家头像

    // 统计信息
    private Integer viewCount;
    private Integer favoriteCount;
    private LocalDateTime publishTime;
    private LocalDateTime updateTime;

    // 交互状态
//    private Boolean isFavorite;                     // 当前用户是否收藏

    // 关联信息
    private List<ItemListItemVO> relatedItems;      // 相关商品推荐

    public List<ItemListItemVO> convertToVO(List<ItemPO> itemPOs) {
        return itemPOs.stream()
                .map(po -> {
                    ItemListItemVO vo = new ItemListItemVO();
                    // 复制相同字段
                    BeanUtils.copyProperties(po, vo);

                    // 处理特殊字段
                    vo.setConditionDesc(getConditionDesc(po.getCondition()));
                    // 其他特殊处理

                    return vo;
                })
                .collect(Collectors.toList());
    }

    // 成色描述转换方法
    private String getConditionDesc(Integer condition) {
        switch (condition) {
            case 1: return "全新";
            case 2: return "9成新";
            case 3: return "8成新";
            case 4: return "7成新及以下";
            default: return "未知";
        }
    }
}