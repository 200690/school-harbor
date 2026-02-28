package com.harbor.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.comment.domain.dto.CommentQueryDTO;
import com.harbor.comment.domain.po.CommentsPO;
import com.harbor.comment.domain.vo.CommentVO;
import com.harbor.common.domain.PageDTO;

public interface ICommentService extends IService<CommentsPO> {
    PageDTO<CommentVO> showMyCommentsGiven(CommentQueryDTO queryDTO);
}
