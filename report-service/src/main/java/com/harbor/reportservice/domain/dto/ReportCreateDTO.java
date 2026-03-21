package com.harbor.reportservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ReportCreateDTO {

    @NotNull(message = "举报类型不能为空")
    private Integer reportType;

    @NotNull(message = "被举报对象ID不能为空")
    private Long targetId;

    @NotNull(message = "举报原因类型不能为空")
    private Integer reasonType;

    @Size(max = 500, message = "原因描述不能超过500字")
    private String reasonDesc;

    private List<String> evidenceImages;
}
