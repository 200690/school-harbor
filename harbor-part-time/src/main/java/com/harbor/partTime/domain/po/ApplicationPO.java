package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("part_time_application")
public class ApplicationPO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 申请记录ID
    private Long partTimeId;                // 兼职ID
    private Long userId;                    // 申请人用户ID
    private Long resumeId;                  // 使用的简历ID
    private LocalDateTime applyTime;        // 申请时间
    private Integer status;                  // 状态：0-待审核，1-已通过，2-已拒绝，3-已取消，4-已完成
    private LocalDateTime interviewTime;     // 面试时间
    private String interviewLocation;        // 面试地点/方式
    private String remark;                   // 备注
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;        // 更新时间
}