package com.harbor.secondHand.service;

import com.harbor.common.domain.PageDTO;
import com.harbor.secondHand.domain.dto.MessageDTO;
import com.harbor.secondHand.domain.vo.MessageListVO;
import com.harbor.secondHand.domain.vo.MessageVO;

public interface IMessageService {
    /**
     * 获取与指定用户的聊天记录
     * @param otherUserId 对方用户ID
     * @param currentUserId 当前用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 聊天记录分页列表
     */
    PageDTO<MessageVO> getChatHistory(Long otherUserId, Long currentUserId, Integer page, Integer size);

    /**
     * 获取聊天会话列表
     * @param currentUserId 当前用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 聊天会话列表
     */
    PageDTO<MessageListVO> getMessageList(Long currentUserId, Integer page, Integer size);

    /**
     * 发送消息
     * @param messageDTO 消息DTO
     */
    void sendMessage(MessageDTO messageDTO);
}
