package com.harbor.comment.controller;

import com.harbor.comment.domain.dto.CommentQueryDTO;
import com.harbor.comment.domain.vo.CommentVO;
import com.harbor.comment.service.ICommentService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/showMyComments")
    public Result<PageDTO<CommentVO>> showMyCommentsGiven(@RequestBody @Validated CommentQueryDTO queryDTO){
        log.info("显示我的评论");
        return Result.success(commentService.showMyCommentsGiven(queryDTO));
    }
}
