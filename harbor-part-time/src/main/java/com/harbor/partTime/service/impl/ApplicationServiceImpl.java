package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;
import com.harbor.partTime.domain.vo.ApplicationerVO;
import com.harbor.partTime.mapper.ApplicationMapper;
import com.harbor.partTime.mapper.PartTimeMapper;
import com.harbor.partTime.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, ApplicationPO> implements IApplicationService {

    private final PartTimeMapper partTimeMapper;


    /**
     * 获取用户申请记录集合
     *
     * @param pageQuery
     * @return
     */
    @Override
    public PageDTO<ApplicationRecordVO> getMyApplications(PageQuery pageQuery) {
        log.info("获取用户申请记录: {}", pageQuery.getId());
        Assert.notNull(pageQuery.getId(), "用户ID不能为空");

        Page<ApplicationPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        Page<ApplicationPO> applicationPage = lambdaQuery()
                .eq(ApplicationPO::getUserId, pageQuery.getId())
                .orderByDesc(ApplicationPO::getUpdateTime) // 按更新时间倒序
                .page(page);
        if (applicationPage.getRecords().isEmpty()) {
            return null;
        }
        // 4. 转换为VO列表
        List<ApplicationRecordVO> recordVOList = applicationPage.getRecords().stream()
                .map(item -> BeanUtil.copyProperties(item, ApplicationRecordVO.class))
                .toList();
        // 5. 获取兼职信息
        List<Long> jobsId = recordVOList.stream()
                .map(ApplicationRecordVO::getPartTimeId)
                .distinct()
                .collect(Collectors.toList());

        List<PartTimePO> partTimePOS = partTimeMapper.selectList(new LambdaQueryWrapper<PartTimePO>()
                .in(PartTimePO::getId, jobsId));

        Map<Long, PartTimePO> partTimePOMap = partTimePOS.stream()
                .collect(Collectors.toMap(PartTimePO::getId, Function.identity()));
// 6. 封装兼职信息
        recordVOList.forEach(item -> {
            PartTimePO partTimePO = partTimePOMap.get(item.getPartTimeId());
            if (partTimePO != null) {
                item.setPartTimeTitle(partTimePO.getTitle());
                item.setPartTimeLocation(partTimePO.getLocation());
                item.setEmployer(partTimePO.getEmployer());
                item.setSalaryDesc(partTimePO.getSalaryDesc());
            }
        });

        // 7. 返回分页结果
        return new PageDTO<>(
                applicationPage.getTotal(),
                applicationPage.getPages(),
                recordVOList
        );

    }

    @Override
    @Transactional
    public void cancelApply(Long partTimeId) {
//        申请表设置已取消
        ApplicationPO one = lambdaQuery().eq(ApplicationPO::getPartTimeId, partTimeId).in(ApplicationPO::getStatus, (Object) new int[]{1, 0}).one();
        Assert.notNull(one, "该兼职申请无法取消");
        one.setStatus(3);
        this.updateById(one);
//        兼职表申请人--
        PartTimePO partTimePO = partTimeMapper.selectById(partTimeId);
        partTimePO.setApplicantCount(partTimePO.getApplicantCount() - 1);
        partTimeMapper.updateById(partTimePO);
    }

    @Override
    public PageDTO<ApplicationerVO> getPartTimeApplyList(PageQuery pageQuery) {
        Assert.notNull(pageQuery.getId(), "用户ID不能为空");
        Page<ApplicationPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        Page<ApplicationPO> applicationPOPage = lambdaQuery()
                .eq(ApplicationPO::getPartTimeId, pageQuery.getId())
                .orderByDesc(ApplicationPO::getUpdateTime)
                .page(page);
        List<ApplicationerVO> voList = applicationPOPage.getRecords().stream().map(applicationPO ->
                BeanUtil.copyProperties(applicationPO, ApplicationerVO.class)
        ).toList();

        return new PageDTO<>(applicationPOPage.getTotal(), applicationPOPage.getPages(), voList);
    }

}
