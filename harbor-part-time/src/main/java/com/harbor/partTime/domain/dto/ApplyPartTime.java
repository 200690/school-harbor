package com.harbor.partTime.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApplyPartTime {
    private Long partTimeId;
    private String resume; // 简历内容
    private String remark; // 备注信息
    private String userName; // 申请人姓名
    private String phone; // 申请人联系电话
}
