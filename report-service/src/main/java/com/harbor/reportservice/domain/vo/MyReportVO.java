package com.harbor.reportservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyReportVO {

    private Long id;

    private Integer reportType;

    private String reportTypeName;

    private Long targetId;

    private Integer reasonType;

    private String reasonTypeName;

    private String reasonDesc;

    private Integer status;

    private String statusName;

    private String handleResult;

    private LocalDateTime createdAt;

    private LocalDateTime handleTime;
}
