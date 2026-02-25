package com.harbor.partTime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;

import java.util.List;

public interface IApplicationService extends IService<ApplicationPO> {

    List<ApplicationRecordVO> getMyApplications(Long id);

    void cancelApply(Long partTimeId);
}
