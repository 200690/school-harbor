package com.harbor.partTime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.partTime.domain.po.ReviewPO;
import com.harbor.partTime.mapper.ReviewMapper;
import com.harbor.partTime.service.IReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, ReviewPO> implements IReviewService {

}
