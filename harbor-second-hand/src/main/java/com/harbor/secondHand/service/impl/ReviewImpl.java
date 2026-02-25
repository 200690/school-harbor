package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.ReviewPO;
import com.harbor.secondHand.mapper.ReviewMapper;
import com.harbor.secondHand.service.IReview;
import org.springframework.stereotype.Service;

@Service
public class ReviewImpl extends ServiceImpl<ReviewMapper, ReviewPO> implements IReview {
}
