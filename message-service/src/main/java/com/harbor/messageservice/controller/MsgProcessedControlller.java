package com.harbor.messageservice.controller;

import com.harbor.common.result.Result;
import com.harbor.messageservice.domain.vo.MessageVO;
import com.harbor.messageservice.service.IMsgProcessedService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@ApiOperation(value = "消息消费记录")
@RequestMapping("message")
public class MsgProcessedControlller {

    private final IMsgProcessedService msgProcessedService;

    @GetMapping("getMymsg/{id}")
    public Result<List<MessageVO>> getMymsg(@PathVariable Long id) {
        log.info("获取用户消息, userId: {}", id);
        return Result.success(msgProcessedService.getMyMsg(id));
    }

    @PostMapping("save/{id}/{text}")
    public Result save(@PathVariable Long id, @PathVariable String text) {
        msgProcessedService.saveMsg(id, text);
        return Result.success();
    }
}
