package com.harbor.secondHand.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.user.domain.po.MessageOutboxPo;
import com.harbor.secondHand.user.mapper.MsgMapper;
import com.harbor.secondHand.user.service.IMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MsgImpl extends ServiceImpl<MsgMapper, MessageOutboxPo> implements IMsgService {
}
