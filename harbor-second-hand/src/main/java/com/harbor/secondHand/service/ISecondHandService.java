package com.harbor.secondHand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.dto.ItemCreateDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.domain.vo.MyItem;
import com.harbor.utils.dto.ItemMainDTO;

public interface ISecondHandService extends IService<ItemPO> {

    PageDTO<ItemListItemVO> querySecondHandItemList(ItemQueryConditionDTO itemQueryConditionDTO);

    ItemDetailVO getItemById(Long id);

    void createItem(ItemCreateDTO itemCreateDTO);

    PageDTO<MyItem> getMyItems(PageQuery id);

    void updateStatus(Long id, Integer status);

    void updateItem(ItemCreateDTO item);

    ItemMainDTO getItemMain(Long id);

    void removeItemById(Long id);
}
