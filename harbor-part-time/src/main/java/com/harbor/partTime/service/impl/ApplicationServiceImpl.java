package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;
import com.harbor.partTime.domain.vo.MyJobs;
import com.harbor.partTime.mapper.ApplicationMapper;
import com.harbor.partTime.service.IApplicationService;
import com.harbor.partTime.service.IPartTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, ApplicationPO> implements IApplicationService {
    private final IPartTimeService partTimeService;

    @Override
    public List<ApplicationRecordVO> getMyApplications(Long userId) {
        log.info("获取用户申请记录: {}", userId);
        Assert.notNull(userId, "用户ID不能为空");

//        获取申请记录
        List<ApplicationRecordVO> recordVOList = lambdaQuery().eq(ApplicationPO::getUserId, userId).list().stream().map(item ->
        BeanUtil.copyProperties(item, ApplicationRecordVO.class)
        ).toList();
        if(recordVOList.isEmpty()){
            return recordVOList;
        }
//        获取兼职信息
        List<Long> jobsId = recordVOList.stream().map(ApplicationRecordVO::getPartTimeId).distinct().toList();
        List<PartTimePO> partTimePOS = partTimeService.getJobsById(jobsId);
        Map<Long, PartTimePO> partTimePOMap = partTimePOS.stream().collect(Collectors.toMap(PartTimePO::getId, Function.identity()));

        recordVOList.forEach(item -> {
            PartTimePO partTimePO = partTimePOMap.get(item.getPartTimeId());
            item.setPartTimeTitle(partTimePO.getTitle());
            item.setPartTimeLocation(partTimePO.getLocation());
            item.setEmployer(partTimePO.getEmployer());
            item.setSalaryDesc(partTimePO.getSalaryDesc());
        });
        return recordVOList;
    }
}
