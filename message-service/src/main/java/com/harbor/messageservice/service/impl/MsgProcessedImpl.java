package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.mapper.MsgProcessedMapper;
import com.harbor.messageservice.service.IMsgProcessedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MsgProcessedImpl extends ServiceImpl<MsgProcessedMapper, MessageProcessedPO> implements IMsgProcessedService {
}
