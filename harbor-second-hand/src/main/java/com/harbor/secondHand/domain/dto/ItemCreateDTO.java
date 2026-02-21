package com.harbor.secondHand.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemCreateDTO {
    @NotBlank(message = "标题不能为空")
    String title;

    String description;

    @NotNull(message = "分类ID不能为空")
    Integer categoryId;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    Double price;

    Double originalPrice;

    @NotNull(message = "商品成色不能为空")
    Integer condition; // 或者使用 ItemCondition 枚举

    @NotBlank(message = "学校不能为空")
    String school;

    String location;
}
