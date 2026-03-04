package com.harbor.partTime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;
import com.harbor.partTime.domain.vo.ApplicationerVO;

public interface IApplicationService extends IService<ApplicationPO> {

    PageDTO<ApplicationRecordVO> getMyApplications(PageQuery id);

    void cancelApply(Long partTimeId);

    PageDTO<ApplicationerVO> getPartTimeApplyList(PageQuery pageQuery);

}
