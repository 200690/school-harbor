package com.harbor.reportservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportHandleDTO {

    @NotNull(message = "举报ID不能为空")
    private Long reportId;

    @NotNull(message = "处理动作不能为空")
    private Integer action;

    private Integer reportType;
}
