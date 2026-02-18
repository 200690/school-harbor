package com.harbor.user.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 拉黑商品请求DTO
 * 用于用户主动拉黑商品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockProductRequestDTO {

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Positive(message = "商品ID必须为正数")
    private Long productId;

    /**
     * 拉黑原因（可选）
     */
    private String reason;
}