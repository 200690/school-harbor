package com.harbor.partTime.domain.dto;

import com.harbor.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class adminPageDTO extends PageQuery {
    private String title;
    private String description;
    private Integer status;
}
