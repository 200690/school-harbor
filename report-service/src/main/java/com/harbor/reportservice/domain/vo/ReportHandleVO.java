package com.harbor.reportservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportHandleVO {

    private Long id;

    private Long reportId;

    private Long handlerId;

    private String handlerName;

    private Integer action;

    private String actionName;

    private String handleDesc;

    private LocalDateTime createdAt;
}
