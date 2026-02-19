package com.harbor.partTime.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 兼职查询参数DTO
 */
@Data
public class PartTimeQueryDTO {

    private String keyword;                // 关键词搜索（标题、描述）

    private List<Integer> types;            // 兼职类型列表（1:校内, 2:校外, 3:实习）

    private Integer creditScore;         // 信誉分属性，差（0-60），中等（61-80），优秀（81-100）

    private String sortField;               // 排序字段（发布时间publishTime, 浏览量viewCount, 申请人数applicantCount, 信誉分creditScore）

    private String sortOrder;                // 排序方式（asc/desc）

    // 分页参数
    private Integer page = 1;                // 当前页码

    private Integer size = 10;                // 每页大小
}