package com.harbor.common.domain;

import lombok.Data;

@Data
public class PageQuery {
    private Long id;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
