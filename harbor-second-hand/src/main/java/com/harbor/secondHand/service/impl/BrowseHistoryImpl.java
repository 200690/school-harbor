package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.BrowseHistoryPO;
import com.harbor.secondHand.mapper.BrowseHistoryMapper;
import com.harbor.secondHand.service.IBrowseHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrowseHistoryImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistoryPO> implements IBrowseHistory {

}
