package com.harbor.secondHand.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel(description = "充值表单实体")
public class RechargeDTO {
    @ApiModelProperty(value = "充值金额", required = true)
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @NotNull(message = "充值金额不能为空")
    private int amount;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
}
