package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ApplicationerVO {
    // 申请表相关信息
    private Long id;
    private Long partTimeId;
    private Long userId;
    private String resume;
    private LocalDateTime applyTime;
    private Integer status; // 状态：0-待审核，1-已通过，2-已拒绝，3-已取消，4-已完成
    private String title;
    private String remark;
    private String companyName;
    private String companyPhone;
}
