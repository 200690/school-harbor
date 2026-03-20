package com.harbor.secondHand.domain.dto;

import com.harbor.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ItemPageDTO extends PageQuery {
    private String title;
    private Integer status;
    private Integer condition;
}
