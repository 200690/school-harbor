package com.harbor.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.comment.domain.po.CommentsPO;
import com.harbor.comment.mapper.commentMapper;
import com.harbor.comment.service.ICommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentImpl extends ServiceImpl<commentMapper, CommentsPO> implements ICommentService {
}
