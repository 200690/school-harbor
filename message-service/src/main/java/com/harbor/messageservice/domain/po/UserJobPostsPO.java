package com.harbor.messageservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("user_job_posts")
public class UserJobPostsPO {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private Long jobId;              // 兼职服务中的原始ID
    private Long userId;             // 发布者ID
    private String title;            // 标题
    private String description;      // 描述
    private BigDecimal salary;       // 薪资
    private String location;         // 地点
    private Integer status;          // 状态：0-下架 1-上架 2-已关闭
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime; // 更新时间
    @Version
    private Integer syncVersion;      // 乐观锁版本号
}