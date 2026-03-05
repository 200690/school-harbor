package com.harbor.messageservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.domain.vo.MessageVO;

import java.util.List;

public interface IMsgProcessedService extends IService<MessageProcessedPO> {
    List<MessageVO> getMyMsg(Long userId);
}
