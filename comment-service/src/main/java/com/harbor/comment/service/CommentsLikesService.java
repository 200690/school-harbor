package com.harbor.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.comment.domain.dto.CommentLikeDTO;
import com.harbor.comment.domain.po.CommentLikesPO;

public interface CommentsLikesService extends IService<CommentLikesPO> {

    void like(CommentLikeDTO commentLikeDTO);
}
