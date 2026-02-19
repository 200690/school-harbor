package com.harbor.partTime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.PartTimeVO;

public interface IPartTimeService extends IService<PartTimePO> {

    PageDTO<PartTimeVO> queryPartTimeList(PartTimeQueryDTO partTimeQueryDTO);
}
