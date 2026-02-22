package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class PartTimeListItemVO {
    private Long id;
    private String title;
    private String employer;
    private String location;
    private String workTime;
    private String salaryUnit;             // 简化薪资说明
    private String salaryDesc;             // 简化薪资说明
    private Integer type;
    private String description;
    private LocalDateTime publishTime;
    private Integer viewCount;
    private Integer applicantCount;
    private Integer creditScore;
    private List<String> tags;              // 标签名称列表
    private Boolean isFavorite;             // 当前用户是否收藏
}