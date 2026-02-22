package com.harbor.partTime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.partTime.domain.po.SettlementPO;
import com.harbor.partTime.mapper.SettlementMapper;
import com.harbor.partTime.service.ISettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SettlementServiceImpl extends ServiceImpl<SettlementMapper, SettlementPO> implements ISettlementService {

}
