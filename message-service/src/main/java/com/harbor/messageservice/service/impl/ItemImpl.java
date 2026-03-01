package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.UserItemPostsPO;
import com.harbor.messageservice.mapper.ItemMapper;
import com.harbor.messageservice.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemImpl extends ServiceImpl<ItemMapper, UserItemPostsPO> implements IItemService {
}
