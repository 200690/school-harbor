package com.harbor.comment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbor.comment.domain.dto.CommentCreateDTO;
import com.harbor.comment.domain.dto.CommentQueryDTO;
import com.harbor.comment.domain.po.CommentLikesPO;
import com.harbor.comment.domain.po.CommentsPO;
import com.harbor.comment.domain.vo.CommentVO;
import com.harbor.comment.mapper.CommentsLikes;
import com.harbor.comment.mapper.CommentsLikes;
import com.harbor.comment.mapper.commentMapper;
import com.harbor.comment.service.ICommentService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.utils.UserContext;
import com.harbor.utils.client.PartTimeClient;
import com.harbor.utils.client.SecondHandClient;
import com.harbor.utils.client.UserClient;
import com.harbor.utils.dto.CommentMessageDTO;
import com.harbor.utils.dto.UserInfoDTO;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentImpl extends ServiceImpl<commentMapper, CommentsPO> implements ICommentService {
    private final CommentsLikes commentsLikes;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final PartTimeClient partTimeClient;
    private final SecondHandClient secondHandClient;

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
            queryWrapper.orderByDesc(CommentsPO::getCreateTime);
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
                .orderByAsc(CommentsPO::getCreateTime) // 按时间正序展示
                .list();
        // 4. 按rootId分组（所有子评论的rootId都应该是根评论的ID）
        Map<Long, List<CommentsPO>> childMap = childComments.stream()
                .collect(Collectors.groupingBy(CommentsPO::getRootId));

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
            // 设置子评论（所有rootId等于当前根评论ID的评论）
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(CommentCreateDTO commentDTO) {
        Long currentUserId = UserContext.getUser();
        
        // 获取当前用户信息
        UserInfoDTO currentUser = userClient.info(currentUserId).getData();
        if (currentUser == null) {
            throw new RuntimeException("用户信息不存在");
        }
        
        CommentsPO comment = new CommentsPO();
        comment.setTargetType(commentDTO.getTargetType());
        comment.setTargetId(commentDTO.getTargetId());
        comment.setUserId(currentUserId);
        comment.setUserName(currentUser.getUsername());
        comment.setUserAvatar(currentUser.getImg());
        comment.setContent(commentDTO.getContent());
        comment.setStatus(1);
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        
        // 处理图片列表
        if (commentDTO.getImages() != null && !commentDTO.getImages().isEmpty()) {
            try {
                comment.setImages(objectMapper.writeValueAsString(commentDTO.getImages()));
            } catch (JsonProcessingException e) {
                log.error("图片列表序列化失败", e);
                throw new RuntimeException("图片列表处理失败");
            }
        }
        
        // 判断是评论还是回复
        if (commentDTO.getParentId() != null) {
            // 回复评论
            comment.setParentId(commentDTO.getParentId());
            comment.setLevel(2);
            
            // 设置根评论ID
            comment.setRootId(commentDTO.getRootId());
            
            // 设置被回复用户信息
            comment.setReplyUserId(commentDTO.getParentId());
            comment.setReplyUserName(commentDTO.getReplyUserName());
            
            // 更新父评论的回复数
            this.lambdaUpdate()
                    .eq(CommentsPO::getId, commentDTO.getParentId())
                    .setSql("reply_count = reply_count + 1")
                    .update();
            
            // 更新根评论的回复数
            if (comment.getRootId() != null && !comment.getRootId().equals(commentDTO.getParentId())) {
                this.lambdaUpdate()
                        .eq(CommentsPO::getId, comment.getRootId())
                        .setSql("reply_count = reply_count + 1")
                        .update();
            }
        } else {
            // 一级评论
            comment.setParentId(0L);
            comment.setLevel(1);
            comment.setRootId(null);
            comment.setScore(commentDTO.getScore());
        }
        
        // 保存评论
        this.save(comment);
        log.info("发表评论成功: userId={}, targetType={}, targetId={}", currentUserId, commentDTO.getTargetType(), commentDTO.getTargetId());
        
        // 发送消息通知
        sendCommentNotification(comment, commentDTO);
    }
    
    /**
     * 发送评论通知消息
     */
    private void sendCommentNotification(CommentsPO comment, CommentCreateDTO commentDTO) {
        try {
            CommentMessageDTO messageDTO = new CommentMessageDTO();
            
            // 获取业务类型名称
            String targetTypeName = commentDTO.getTargetType() == 0 ? "兼职" : "商品";
            
            if (commentDTO.getParentId() != null) {
                // 回复通知
                messageDTO.setMessage("您收到了一条" + targetTypeName + "回复");
                // 获取父评论作者ID作为接收人
                CommentsPO parentComment = this.getById(commentDTO.getParentId());
                if (parentComment != null && !parentComment.getUserId().equals(comment.getUserId())) {
                    messageDTO.setToUserId(parentComment.getUserId());
                    rabbitTemplate.convertAndSend(
                            "harbor.exchange",
                            "harbor.comment.notification",
                            messageDTO);
                    log.info("发送回复通知成功: toUserId={}, message={}", messageDTO.getToUserId(), messageDTO.getMessage());
                }
            } else {
                // 评论通知 - 需要获取业务发布者ID
                messageDTO.setMessage("您收到了一条" + targetTypeName + "评论");
                Long targetUserId = getTargetUserId(commentDTO.getTargetType(), commentDTO.getTargetId());
                if (targetUserId != null && !targetUserId.equals(comment.getUserId())) {
                    messageDTO.setToUserId(targetUserId);
                    rabbitTemplate.convertAndSend(
                            "harbor.exchange",
                            "harbor.comment.notification",
                            messageDTO);
                    log.info("发送评论通知成功: toUserId={}, message={}", messageDTO.getToUserId(), messageDTO.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("发送评论通知失败: commentId={}, error={}", comment.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 获取业务发布者ID
     */
    private Long getTargetUserId(Integer targetType, Long targetId) {
        // targetType: 0-兼职, 1-商品
        try {
            if (targetType == 0) {
                // 兼职
                return partTimeClient.getPublisherId(targetId).getData();
            } else if (targetType == 1) {
                // 商品
                return secondHandClient.getSellerId(targetId).getData();
            }
        } catch (Exception e) {
            log.error("获取业务发布者ID失败: targetType={}, targetId={}, error={}", targetType, targetId, e.getMessage());
        }
        return null;
    }

}
