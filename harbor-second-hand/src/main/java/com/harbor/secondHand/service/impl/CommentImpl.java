package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.CommentPO;
import com.harbor.secondHand.mapper.Comment;
import com.harbor.secondHand.service.IComment;
import org.springframework.stereotype.Service;

@Service
public class CommentImpl extends ServiceImpl<Comment, CommentPO> implements IComment {
}
