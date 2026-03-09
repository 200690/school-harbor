package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.domain.dto.MessageDTO;
import com.harbor.secondHand.domain.po.MessagePO;
import com.harbor.secondHand.domain.vo.MessageListVO;
import com.harbor.secondHand.domain.vo.MessageVO;
import com.harbor.secondHand.mapper.MessageMapper;
import com.harbor.secondHand.service.IMessageService;
import com.harbor.utils.client.UserClient;
import com.harbor.utils.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessagePO> implements IMessageService {
    private final UserClient userClient;

    @Override
    public PageDTO<MessageVO> getChatHistory(Long otherUserId, Long currentUserId, Integer page, Integer size) {
        // 构建查询条件，查询双方之间的消息
        LambdaQueryWrapper<MessagePO> queryWrapper = new LambdaQueryWrapper<MessagePO>()
                .and(wrapper -> wrapper
                        .eq(MessagePO::getSenderId, currentUserId)
                        .eq(MessagePO::getReceiverId, otherUserId)
                )
                .or(wrapper -> wrapper
                        .eq(MessagePO::getSenderId, otherUserId)
                        .eq(MessagePO::getReceiverId, currentUserId)
                )
                .eq(MessagePO::getIsDelete, 0) // 只查询未删除的消息
                .orderByAsc(MessagePO::getSendTime); // 按发送时间升序

        // 标记对方发送的未读消息为已读
        baseMapper.markMessagesAsRead(currentUserId, otherUserId);

        // 分页查询
        Page<MessagePO> messagePage = new Page<>(page, size);
        Page<MessagePO> resultPage = page(messagePage, queryWrapper);

        // 转换为MessageVO
        List<MessageVO> messageVOList = resultPage.getRecords().stream().map(this::convertToMessageVO).collect(Collectors.toList());

        // 构建返回的分页对象
        Page<MessageVO> messageVOPage = new Page<>(page, size);
        messageVOPage.setRecords(messageVOList);
        messageVOPage.setTotal(resultPage.getTotal());
        messageVOPage.setPages(resultPage.getPages());

        return new PageDTO<>(messageVOPage.getTotal(), messageVOPage.getPages(), messageVOList);
    }

    private MessageVO convertToMessageVO(MessagePO messagePO) {
        MessageVO messageVO = new MessageVO();
        messageVO.setId(messagePO.getId());
        messageVO.setSenderId(messagePO.getSenderId());
        UserInfoDTO userInfoDTO = userClient.info(messagePO.getSenderId()).getData();
        messageVO.setSenderName(userInfoDTO.getUsername());
        messageVO.setSenderAvatar(userInfoDTO.getImg());
        messageVO.setReceiverId(messagePO.getReceiverId());
        messageVO.setItemId(messagePO.getItemId());
        messageVO.setItemTitle(null);
        messageVO.setContent(messagePO.getContent());
        messageVO.setStatus(messagePO.getStatus());
        messageVO.setSendTime(messagePO.getSendTime());
        messageVO.setReadTime(messagePO.getReadTime());
        return messageVO;
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        MessagePO messagePO = new MessagePO();
        messagePO.setSenderId(UserContext.getUser());
        messagePO.setReceiverId(messageDTO.getReceiverId());
        messagePO.setItemId(messageDTO.getItemId());
        messagePO.setContent(messageDTO.getContent());
        messagePO.setStatus(0); // 0-未读
        messagePO.setSendTime(LocalDateTime.now());
        messagePO.setIsDelete(0); // 0-正常
        this.save(messagePO);
        log.info("发送消息成功");
    }

    @Override
    public PageDTO<MessageListVO> getMessageList(Long currentUserId, Integer page, Integer size) {
        Long offset = (long) (page - 1) * size;
        List<MessagePO> messageList = baseMapper.selectLatestMessagePerSession(currentUserId, offset, (long) size);
        Long total = baseMapper.countDistinctSession(currentUserId);
        Long pages = (total + size - 1) / size;

        List<MessageListVO> messageListVOList = messageList.stream().map(messagePO -> {
            MessageListVO vo = new MessageListVO();
            vo.setId(messagePO.getId());
            vo.setSenderId(messagePO.getSenderId());
            vo.setReceiverId(messagePO.getReceiverId());
            vo.setItemId(messagePO.getItemId());
            vo.setLastMessage(messagePO.getContent());
            vo.setLastMessageTime(messagePO.getSendTime());
            vo.setStatus(messagePO.getStatus());
            vo.setCreateTime(messagePO.getSendTime());

            UserInfoDTO userInfoDTO = userClient.info(messagePO.getSenderId()).getData();
            vo.setSenderName(userInfoDTO.getUsername());
            vo.setAvatar(userInfoDTO.getImg());


            Long otherUserId = messagePO.getSenderId().equals(currentUserId) ? messagePO.getReceiverId() : messagePO.getSenderId();
            vo.setOtherUserId(otherUserId);

            try {
                UserInfoDTO userInfo = userClient.info(otherUserId).getData();
                if (userInfo != null) {
                    vo.setOtherUserNickname(userInfo.getUsername());
                    vo.setOtherUserAvatar(userInfo.getImg());
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败: {}", otherUserId);
            }

            Integer unreadCount = baseMapper.countUnreadMessagesBySession(currentUserId, otherUserId);
            vo.setUnreadCount(unreadCount);

            return vo;
        }).collect(Collectors.toList());

        return new PageDTO<>(total, pages, messageListVOList);
    }
}
