package com.harbor.reportservice.domain.dto;

import com.harbor.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportQueryDTO extends PageQuery {
    // 举报内容
    private String reasonDesc;

    // 举报类型：1-评论，2-商品，3-兼职，4-用户
    private Integer reportType;

    // 状态：0-待处理，1-已处理（违规），2-已处理（正常）
    private Integer status;

    // 理由类型：1-虚假信息，2-诈骗行为，3-色情内容，4-暴力内容，5-违法违规，6-其他
    private Integer reasonType;
}
