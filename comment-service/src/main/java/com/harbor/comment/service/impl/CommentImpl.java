package com.harbor.comment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.comment.domain.dto.CommentQueryDTO;
import com.harbor.comment.domain.po.CommentLikesPO;
import com.harbor.comment.domain.po.CommentsPO;
import com.harbor.comment.domain.vo.CommentVO;
import com.harbor.comment.mapper.CommentsLikes;
import com.harbor.comment.mapper.commentMapper;
import com.harbor.comment.service.ICommentService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.utils.UserContext;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentImpl extends ServiceImpl<commentMapper, CommentsPO> implements ICommentService {
    private final CommentsLikes commentsLikes;

    @Override
    public PageDTO<CommentVO> showMyCommentsGiven(CommentQueryDTO queryDTO) {
        Page<CommentsPO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CommentsPO> queryWrapper = new LambdaQueryWrapper<>();
        if(queryDTO.getType() == 1){
            queryWrapper.eq(CommentsPO::getUserId, queryDTO.getUserId());
        }else if(queryDTO.getType() == 2){
            queryWrapper.eq(CommentsPO::getReplyUserId, queryDTO.getUserId());
        }else{
            throw new RuntimeException("类型错误");
        }

        queryWrapper.eq(CommentsPO::getStatus, 1);
        if(queryDTO.getTargetType() != 2) {
            queryWrapper.eq(CommentsPO::getTargetType, queryDTO.getTargetType());
        }else {
            queryWrapper.in(CommentsPO::getTargetType, 0, 1);
        }
        if(queryDTO.getSortType() == 1){
            queryWrapper.orderByDesc(CommentsPO::getCreatedAt);
        }else if(queryDTO.getSortType() == 2){
            queryWrapper.orderByDesc(CommentsPO::getLikeCount);
        }else{
            throw  new RuntimeException("排序方式错误");
        }
        Page<CommentsPO> commentsPOPage = this.page(page, queryWrapper);

        if(commentsPOPage.getTotal() == 0)
            return null;
        List<CommentVO> commentVOS = commentsPOPage.getRecords().stream().map(commentsPO -> {
            CommentVO commentVO = new CommentVO();
            BeanUtil.copyProperties(commentsPO, commentVO);
            if (StringUtils.isNotBlank(commentsPO.getImages())) {
                commentVO.setImages(Arrays.asList(commentsPO.getImages()));
            }
            //填充是否点赞字段 isLiked
            CommentLikesPO likesPO = commentsLikes.selectOne((new LambdaQueryWrapper<CommentLikesPO>())
                    .eq(CommentLikesPO::getCommentId, commentsPO.getId())
                    .eq(CommentLikesPO::getUserId, commentsPO.getUserId())
            );
            if(likesPO != null)
                commentVO.setIsLiked(likesPO.getStatus() == 1);
            return commentVO;
        }).toList();
        return new PageDTO<>( commentsPOPage.getTotal(), commentsPOPage.getPages(), commentVOS);
    }
}
