package com.harbor.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.comment.domain.po.CommentReportsPO;
import com.harbor.comment.mapper.CommentReportsMapper;
import com.harbor.comment.service.ICommentReportsService;
import org.springframework.stereotype.Service;

@Service
public class CommentReportsImpl extends ServiceImpl<CommentReportsMapper, CommentReportsPO> implements ICommentReportsService {
}
