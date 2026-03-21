package com.harbor.reportservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportVO {

    private Long id;

    private Integer reportType;

    private String reportTypeName;

    private Long targetId;

    private Long reporterId;

    private String reporterName;

    private String reporterAvatar;

    private Integer reasonType;

    private String reasonTypeName;

    private String reasonDesc;

    private List<String> evidenceImages;

    private Integer status;

    private String statusName;

    private Long handlerId;

    private String handlerName;

    private LocalDateTime handleTime;

    private String handleResult;

    private LocalDateTime createdAt;
}
