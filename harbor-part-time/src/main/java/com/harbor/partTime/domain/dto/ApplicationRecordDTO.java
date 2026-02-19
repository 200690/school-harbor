package com.harbor.partTime.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationRecordDTO {
    private Long id;
    private Long partTimeId;
    private String partTimeTitle;         // 兼职标题
    private Long userId;
    private String userName;              // 申请人姓名
    private String userAvatar;             // 申请人头像
    private LocalDateTime applyTime;
    private Integer status;
    private LocalDateTime interviewTime;
    private String interviewLocation;
    private String remark;
}