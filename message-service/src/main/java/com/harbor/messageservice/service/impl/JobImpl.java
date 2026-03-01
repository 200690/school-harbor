package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.UserJobPostsPO;
import com.harbor.messageservice.mapper.JobMapper;
import com.harbor.messageservice.service.IJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobImpl extends ServiceImpl<JobMapper, UserJobPostsPO> implements IJobService {
}
