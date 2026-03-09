package com.harbor.secondHand.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.domain.dto.MessageDTO;
import com.harbor.secondHand.domain.vo.MessageListVO;
import com.harbor.secondHand.domain.vo.MessageVO;
import com.harbor.secondHand.service.IMessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/second-hand")
public class MessageServiceImplController {
    private final IMessageService messageService;

    @ApiOperation("获取聊天会话列表")
    @GetMapping("/messages/list")
    public Result<PageDTO<MessageListVO>> getMessageList(@RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "20") Integer size) {
        Long currentUserId = UserContext.getUser();
        Assert.notNull(currentUserId, "用户未登录");

        PageDTO<MessageListVO> messageList = messageService.getMessageList(currentUserId, page, size);
        return Result.success(messageList);
    }

    @ApiOperation("获取与指定用户的聊天记录")
    @GetMapping("/messages/chat/{otherUserId}")
    public Result<PageDTO<MessageVO>> getChatHistory(@PathVariable Long otherUserId,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "20") Integer size) {
        // 获取当前登录用户ID
        Long currentUserId = UserContext.getUser();
        Assert.notNull(currentUserId, "用户未登录");


        // 调用服务获取聊天记录
        PageDTO<MessageVO> chatHistory = messageService.getChatHistory(otherUserId, currentUserId, page, size);
        return Result.success(chatHistory);
    }

    @ApiOperation("发送消息")
    @PostMapping("/messages/send")
    public Result sendMessage(@RequestBody MessageDTO messageDTO) {
        // 调用服务发送消息
        messageService.sendMessage(messageDTO);
        return Result.success();
    }
}
