package com.harbor.messageservice.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobInfoVO {
    private Long id;              // 兼职ID
    private String title;         // 兼职标题
    private String employer;      // 招聘方
    private String location;      // 工作地点
    private String salaryDesc;    // 薪资说明
    private Integer status;       // 状态
    private LocalDateTime publishTime; // 发布时间
}
