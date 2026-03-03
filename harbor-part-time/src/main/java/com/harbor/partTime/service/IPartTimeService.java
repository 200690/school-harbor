package com.harbor.partTime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.partTime.domain.dto.PartTimeCreateDTO;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.MyJobs;
import com.harbor.partTime.domain.vo.PartTimeDetailVO;
import com.harbor.partTime.domain.vo.PartTimeVO;

import java.util.List;

public interface IPartTimeService extends IService<PartTimePO> {

    PageDTO<PartTimeVO> queryPartTimeList(PartTimeQueryDTO partTimeQueryDTO);

    void newJob(PartTimeCreateDTO partTimeCreateDTO);

    PageDTO<MyJobs> getMyJobs(PageQuery id);

    void updateStatus(Long id, Integer status);

    void updateById(PartTimeCreateDTO partTimeDTO);

    PartTimeDetailVO getJobById(Long id);

    List<PartTimePO> getJobsById(List<Long> ids);

    void removeJobById(Long id);
}
