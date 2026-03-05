package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.domain.vo.MessageVO;
import com.harbor.messageservice.mapper.MsgProcessedMapper;
import com.harbor.messageservice.service.IMsgProcessedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MsgProcessedImpl extends ServiceImpl<MsgProcessedMapper, MessageProcessedPO> implements IMsgProcessedService {

    @Override
    public List<MessageVO> getMyMsg(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        // 查询用户的消息列表，按处理时间倒序排列
        List<MessageProcessedPO> messageList = lambdaQuery()
                .eq(MessageProcessedPO::getUserId, userId)
                .orderByDesc(MessageProcessedPO::getProcessedTime)
                .list();

        // 转换为 VO 对象
        return messageList.stream().map(po -> {
            MessageVO vo = new MessageVO();
            vo.setId(po.getId())
                    .setMessage(po.getMessage())
                    .setProcessedTime(po.getProcessedTime())
                    .setStatus(po.getStatus());
            return vo;
        }).collect(Collectors.toList());
    }
}
