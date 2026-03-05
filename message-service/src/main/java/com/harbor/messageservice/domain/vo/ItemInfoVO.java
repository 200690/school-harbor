package com.harbor.messageservice.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemInfoVO {
    private Long id;              // 商品ID
    private String title;         // 商品标题
    private BigDecimal price;     // 价格
    private String coverImage;    // 封面图片
    private Integer status;       // 状态
    private LocalDateTime publishTime; // 发布时间
}
