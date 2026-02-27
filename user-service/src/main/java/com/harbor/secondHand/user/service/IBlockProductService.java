package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.user.domain.po.UserBlockProductPO;

public interface IBlockProductService extends IService<UserBlockProductPO> {
    PageDTO getMyBlockProducts(PageQuery pageQuery);

    void removeByItemId(Long Itemid);
}
