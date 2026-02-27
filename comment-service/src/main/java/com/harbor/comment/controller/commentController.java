package com.harbor.comment.controller;

import com.harbor.comment.service.ICommentService;
import com.harbor.common.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@Slf4j
@ApiOperation("评论相关接口")
@RequiredArgsConstructor
public class commentController {
    private final ICommentService commentService;

    @ApiOperation("显示发出我的评论")
    @PostMapping("/showMyComments/given")
    public Result showMyCommentsGiven(){

        return Result.success();
    }

    @ApiOperation("显示收到的我的评论")
    @PostMapping("showMyComments/received")
    public Result showMyCommentsReceived(){

        return Result.success();
    }
}
