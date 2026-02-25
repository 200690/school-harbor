package com.harbor.common.domain;

import lombok.Data;

@Data
public class PageQuery {
    private Long id;
    private Integer pageNum;
    private Integer pageSize;
}
