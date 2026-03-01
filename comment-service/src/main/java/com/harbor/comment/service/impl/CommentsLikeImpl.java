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
//        查询是否有记录存在
        if(commentLikesPO == null){
            if(commentLikeDTO.getStatus() == 0)
                return;
            commentLikesPO = new CommentLikesPO();
            commentLikesPO.setCommentId(commentLikeDTO.getCommentId())
                    .setUserId(UserContext.getUser())
                    .setStatus(1);
            try {
                if (this.save(commentLikesPO)) {
                    commentService.lambdaUpdate()
                            .eq(CommentsPO::getId, commentLikeDTO.getCommentId())
                            .setSql("like_count = like_count + " + 1)
                            .update();
                }
            } catch (DuplicateKeyException ignored) {
            }
        }
//        记录存在逻辑
        else{
            lock.lock();
            if(Objects.equals(commentLikeDTO.getStatus(), commentLikesPO.getStatus()))
                return;
            commentLikesPO.setStatus(commentLikeDTO.getStatus());
            if(this.updateById(commentLikesPO)){
                try {
                    int delta = commentLikeDTO.getStatus() == 1 ? 1 : -1;
                    commentService.lambdaUpdate()
                            .eq(CommentsPO::getId, commentLikeDTO.getCommentId())
                            .setSql("like_count = like_count + " + delta)
                            .update();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
