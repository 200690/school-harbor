package com.harbor.partTime.domain.DTO;

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

    private String location;                // 工作地点（模糊查询）

    private List<Integer> statusList;       // 状态列表

    private Long publisherId;               // 发布者ID

    private Integer minCreditScore;         // 最低信誉分

    private Integer maxCreditScore;         // 最高信誉分

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startPublishTime; // 发布开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPublishTime;   // 发布结束时间

    private String sortField;               // 排序字段（publishTime, viewCount, applicantCount, creditScore）

    private String sortOrder;                // 排序方式（asc/desc）

    // 分页参数
    private Integer page = 1;                // 当前页码

    private Integer size = 10;                // 每页大小
}