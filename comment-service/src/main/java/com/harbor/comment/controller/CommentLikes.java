package com.harbor.comment.controller;

import com.harbor.comment.domain.dto.CommentLikeDTO;
import com.harbor.comment.service.CommentsLikesService;
import com.harbor.common.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment/commentLike")
@Slf4j
@ApiOperation("点赞相关接口")
@RequiredArgsConstructor
public class CommentLikes {
    private final CommentsLikesService commentsLikesService;

    @PostMapping("/like")
    @ApiOperation("点赞")
    public Result like(@RequestBody CommentLikeDTO commentLikeDTO) {
        commentsLikesService.like(commentLikeDTO);
        log.info("点赞成功");
        return Result.success();
    }
}
