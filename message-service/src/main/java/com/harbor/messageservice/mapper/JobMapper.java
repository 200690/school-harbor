package com.harbor.messageservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.messageservice.domain.po.UserJobPostsPO;

public interface JobMapper extends BaseMapper<UserJobPostsPO> {
  UserJobPostsPO selectByJobId(Long jobId);

  void deleteByJobId(Long jobId);
}
