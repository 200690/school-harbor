package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ApplicationRecordVO {
    private Long id;
    private Long partTimeId;                // 兼职ID
    private String partTimeTitle;               // 兼职标题
    private String partTimeLocation;            // 兼职地点
    private String employer;                    // 发布者
    private Integer publisherId;
    private String salaryDesc;                  // 薪资描述
    private LocalDateTime applyTime;        // 申请时间
    private Integer status;                  // 申请状态(状态：0-待审核，1-已通过，2-已拒绝，3-已取消，4-已完成)
    private LocalDateTime interviewTime;    // 面试时间
    private String interviewLocation;       // 面试地点/方式
    private String remark;                  // 备注
}