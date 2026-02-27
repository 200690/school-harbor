package com.harbor.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.comment.domain.po.CommentLikesPO;
import com.harbor.comment.mapper.CommentsLikes;
import com.harbor.comment.service.CommentsLikesService;
import org.springframework.stereotype.Service;

@Service
public class CommentsLikeImpl extends ServiceImpl<CommentsLikes, CommentLikesPO> implements CommentsLikesService {
}
