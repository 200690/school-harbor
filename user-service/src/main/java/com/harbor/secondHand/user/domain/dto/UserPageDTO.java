package com.harbor.secondHand.user.domain.dto;

import com.harbor.common.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserPageDTO extends PageQuery {
    private String userName;
    private String phone;
    private String role;
    private Integer status;
}
