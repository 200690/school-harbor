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

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public PageDTO<CommentVO> showComments(CommentQueryDTO queryDTO) {
        Page<CommentsPO> page = new Page<CommentsPO>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<CommentsPO> commentsPOPage = lambdaQuery().eq(CommentsPO::getStatus, 1)
                .eq(CommentsPO::getTargetType, queryDTO.getTargetType())
                .eq(CommentsPO::getLevel, 1)
                .eq(CommentsPO::getTargetId, queryDTO.getTargetId())
                .orderByDesc(CommentsPO::getReplyCount)
                .page(page);
        List<CommentsPO> rootComments = commentsPOPage.getRecords();
        if (rootComments == null || rootComments.isEmpty()) {
            // 无数据时返回空页
            return null;
        }
        List<Long> rootIds = rootComments.stream()
                .map(CommentsPO::getId)
                .toList();

        // 3. 批量查询子评论（这里仅查询直接子评论，即 level=2）
        //    若系统支持多级嵌套，可改为递归查询所有后代评论，但需注意性能
        List<CommentsPO> childComments = lambdaQuery()
                .eq(CommentsPO::getStatus, 1)
                .eq(CommentsPO::getTargetType, queryDTO.getTargetType())
                .eq(CommentsPO::getTargetId, queryDTO.getTargetId())
                .in(CommentsPO::getRootId, rootIds)
                .orderByAsc(CommentsPO::getCreatedAt) // 按时间正序展示
                .list();
        // 4. 按父评论ID分组
        Map<Long, List<CommentsPO>> childMap = childComments.stream()
                .collect(Collectors.groupingBy(CommentsPO::getParentId));

        // 5. 组装VO
        List<CommentVO> voList = new ArrayList<>();
        Long currentUserId = UserContext.getUser();
        for (CommentsPO root : rootComments) {
            CommentVO vo = BeanUtil.copyProperties(root, CommentVO.class);
            // 填充是否点赞字段 isLiked
            CommentLikesPO likesPO = commentsLikes.selectOne((new LambdaQueryWrapper<CommentLikesPO>())
                    .eq(CommentLikesPO::getCommentId, root.getId())
                    .eq(CommentLikesPO::getUserId, currentUserId)
            );
            if(likesPO != null)
                vo.setIsLiked(likesPO.getStatus() == 1);
            // 设置子评论
            List<CommentsPO> children = childMap.getOrDefault(root.getId(), Collections.emptyList());
            List<CommentVO> childVOs = children.stream()
                    .map(po -> {
                        CommentVO childVO = BeanUtil.copyProperties(po, CommentVO.class);
                        // 填充子评论是否点赞字段 isLiked
                        CommentLikesPO childLikesPO = commentsLikes.selectOne((new LambdaQueryWrapper<CommentLikesPO>())
                                .eq(CommentLikesPO::getCommentId, po.getId())
                                .eq(CommentLikesPO::getUserId, currentUserId)
                        );
                        if(childLikesPO != null)
                            childVO.setIsLiked(childLikesPO.getStatus() == 1);
                        return childVO;
                    })
                    .collect(Collectors.toList());
            vo.setChildren(childVOs);
            voList.add(vo);
        }

        return new PageDTO<>(commentsPOPage.getTotal(), commentsPOPage.getPages(), voList);
    }

}
