package com.harbor.partTime.domain.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 兼职状态更新DTO
 */
@Data
public class PartTimeStatusDTO {

    @NotNull(message = "兼职ID不能为空")
    private Long id;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值错误")
    @Max(value = 3, message = "状态值错误")
    private Integer status;
}