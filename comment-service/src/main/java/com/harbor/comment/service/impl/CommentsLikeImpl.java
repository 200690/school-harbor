package com.harbor.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.comment.domain.dto.CommentLikeDTO;
import com.harbor.comment.domain.po.CommentLikesPO;
import com.harbor.comment.domain.po.CommentsPO;
import com.harbor.comment.mapper.CommentsLikes;
import com.harbor.comment.service.CommentsLikesService;
import com.harbor.comment.service.ICommentService;
import com.harbor.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CommentsLikeImpl extends ServiceImpl<CommentsLikes, CommentLikesPO> implements CommentsLikesService {
    private final ICommentService commentService;

    private final Lock lock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void like(CommentLikeDTO commentLikeDTO) {
        // 1. 创建实体
        CommentLikesPO commentLikesPO = lambdaQuery().eq(CommentLikesPO::getCommentId, commentLikeDTO.getCommentId())
                .eq(CommentLikesPO::getUserId, UserContext.getUser())
                .one();
        if(commentLikesPO == null){
            commentLikesPO = new CommentLikesPO();
            commentLikesPO.setCommentId(commentLikeDTO.getCommentId())
                    .setUserId(UserContext.getUser())
                    .setStatus(commentLikeDTO.getStatus());
            try {
                if (this.save(commentLikesPO)) {
                    lock.lock();
                    try {
                        CommentsPO commentServiceById = commentService.getById(commentLikeDTO.getCommentId());
                        commentServiceById.setLikeCount(commentServiceById.getLikeCount() + 1);
                        commentService.updateById(commentServiceById);
                    }finally {
                        lock.unlock();
                    }
                }
            } catch (DuplicateKeyException ignored) {
            }
        }else{
            if(Objects.equals(commentLikeDTO.getStatus(), commentLikesPO.getStatus()))
                return;
            commentLikesPO.setStatus(commentLikeDTO.getStatus());
            if(this.updateById(commentLikesPO)){
                lock.lock();
                try {
                    CommentsPO commentServiceById = commentService.getById(commentLikeDTO.getCommentId());
                    if(commentLikeDTO.getStatus() == 1){
                        commentServiceById.setLikeCount(commentServiceById.getLikeCount() + 1);
                    } else if (commentLikeDTO.getStatus() == 0) {
                        commentServiceById.setLikeCount(commentServiceById.getLikeCount() - 1);
                    }
                    commentService.updateById(commentServiceById);
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
