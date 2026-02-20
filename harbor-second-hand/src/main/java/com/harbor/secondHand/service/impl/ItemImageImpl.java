package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.ItemImagePO;
import com.harbor.secondHand.mapper.ItemImage;
import com.harbor.secondHand.service.IItemImage;
import org.springframework.stereotype.Service;

@Service
public class ItemImageImpl extends ServiceImpl<ItemImage, ItemImagePO> implements IItemImage {
}
