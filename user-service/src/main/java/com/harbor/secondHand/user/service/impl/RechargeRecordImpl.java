package com.harbor.secondHand.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.user.domain.po.RechargeRecord;
import com.harbor.secondHand.user.mapper.RechargeRecordMapper;
import com.harbor.secondHand.user.service.IRechargeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RechargeRecordImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements IRechargeRecordService {
}
