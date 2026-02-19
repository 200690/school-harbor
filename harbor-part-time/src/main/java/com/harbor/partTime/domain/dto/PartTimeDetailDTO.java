package com.harbor.partTime.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PartTimeDetailDTO {
    private Long id;
    private String title;
    private String employer;
    private String location;
    private String workTime;
    private String salaryUnit;
    private String salaryDesc;
    private Integer type;
    private String description;
    private String requirements;
    private String contactPerson;
    private String contactPhone;
    private Long publisherId;
    private String publisherName;        // 发布者名称（来自用户服务）
    private String publisherAvatar;       // 发布者头像
    private Integer status;
    private LocalDateTime publishTime;
    private Integer viewCount;
    private Integer applicantCount;
    private Integer creditScore;
    private List<String> tags;            // 标签名称列表
}