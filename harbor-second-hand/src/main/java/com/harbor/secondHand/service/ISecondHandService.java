package com.harbor.secondHand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;

public interface ISecondHandService extends IService<ItemPO> {

    PageDTO<ItemListItemVO> querySecondHandItemList(ItemQueryConditionDTO itemQueryConditionDTO);

    ItemDetailVO getItemById(Long id);
}
