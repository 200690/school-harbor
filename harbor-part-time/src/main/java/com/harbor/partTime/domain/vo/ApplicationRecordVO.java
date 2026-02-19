package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ApplicationRecordVO {
    private Long id;
    private Long partTimeId;
    private String partTimeTitle;
    private String partTimeLocation;
    private String employer;
    private String salaryDesc;
    private LocalDateTime applyTime;
    private Integer status;                  // 申请状态
    private String statusDesc;                // 状态描述（如“待审核”、“已通过”等）
    private LocalDateTime interviewTime;
    private String interviewLocation;
    private String remark;
}