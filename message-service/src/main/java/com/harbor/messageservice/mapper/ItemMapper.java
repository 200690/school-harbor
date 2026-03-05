package com.harbor.messageservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.messageservice.domain.po.UserItemPostsPO;

public interface ItemMapper extends BaseMapper<UserItemPostsPO> {
    UserItemPostsPO selectByItemId(Long itemId);
}
