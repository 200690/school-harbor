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
import com.harbor.partTime.producer.ApplicationMessageProducer;
import com.harbor.partTime.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import com.harbor.common.utils.UserContext;
import com.harbor.partTime.domain.dto.ApplyPartTime;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, ApplicationPO>
        implements IApplicationService {

    private final PartTimeMapper partTimeMapper;
    private final ApplicationMessageProducer applicationMessageProducer;
    private final RedisTemplate<String, Object> redisTemplate;

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
                recordVOList);

    }

    @Override
    @Transactional
    public void cancelApply(Long partTimeId) {
        // 申请表设置已取消
        ApplicationPO one = lambdaQuery().eq(ApplicationPO::getPartTimeId, partTimeId)
                .in(ApplicationPO::getStatus, (Object) new int[] { 1, 0 }).one();
        Assert.notNull(one, "该兼职申请无法取消");
        one.setStatus(3);
        this.updateById(one);
        // 兼职表申请人--
        PartTimePO partTimePO = partTimeMapper.selectById(partTimeId);
        partTimePO.setApplicantCount(partTimePO.getApplicantCount() - 1);
        partTimeMapper.updateById(partTimePO);
        // 清楚redis
        String detailCacheKey = "job:detail:" + partTimeId;
        redisTemplate.delete(detailCacheKey);
        // 清除推荐列表缓存，因为申请人数变化可能影响推荐
        String recommendCacheKey = "job:list:recommend";
        redisTemplate.delete(recommendCacheKey);
        log.info("取消申请成功，清除兼职缓存: detail={}, recommend={}", partTimeId, recommendCacheKey);
    }

    @Override
    public PageDTO<ApplicationerVO> getPartTimeApplyList(PageQuery pageQuery) {
        Assert.notNull(pageQuery.getId(), "用户ID不能为空");
        Page<ApplicationPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        Page<ApplicationPO> applicationPOPage = lambdaQuery()
                .eq(ApplicationPO::getPartTimeId, pageQuery.getId())
                .orderByDesc(ApplicationPO::getUpdateTime)
                .page(page);
        List<ApplicationerVO> voList = applicationPOPage.getRecords().stream()
                .map(applicationPO -> BeanUtil.copyProperties(applicationPO, ApplicationerVO.class)).toList();

        return new PageDTO<>(applicationPOPage.getTotal(), applicationPOPage.getPages(), voList);
    }

    @Override
    public void approveApply(Long applicationId) {
        Assert.notNull(applicationId, "申请ID不能为空");
        ApplicationPO one = lambdaQuery().eq(ApplicationPO::getId, applicationId).one();
        Assert.notNull(one, "申请不存在");
        if (one.getStatus() == 0) {
            one.setStatus(1);
        } else if (one.getStatus() == 1) {
            return;
        } else {
            throw new RuntimeException("申请状态异常");
        }
        this.updateById(one);

        // 获取兼职信息，用于构建消息
        PartTimePO partTimePO = partTimeMapper.selectById(one.getPartTimeId());
        if (partTimePO != null) {
            // 发送消息队列通知用户申请通过
            applicationMessageProducer.sendApplicationApproveMessage(
                    applicationId,
                    one.getPartTimeId(),
                    one.getUserId(),
                    partTimePO.getTitle());
        } else {
            log.warn("兼职信息不存在，无法发送审批通知，partTimeId: {}", one.getPartTimeId());
        }
    }

    @Override
    public void rejectApply(Long applicationId) {
        Assert.notNull(applicationId, "申请ID不能为空");
        ApplicationPO one = lambdaQuery().eq(ApplicationPO::getId, applicationId).one();
        Assert.notNull(one, "申请不存在");
        if (one.getStatus() == 0) {
            one.setStatus(2);
        } else if (one.getStatus() == 2) {
            return;
        } else {
            throw new RuntimeException("申请状态异常");
        }
        this.updateById(one);

        // 获取兼职信息，用于构建消息
        PartTimePO partTimePO = partTimeMapper.selectById(one.getPartTimeId());
        if (partTimePO != null) {
            // 发送消息队列通知用户申请被拒绝
            applicationMessageProducer.sendApplicationRejectMessage(
                    applicationId,
                    one.getPartTimeId(),
                    one.getUserId(),
                    partTimePO.getTitle());
        } else {
            log.warn("兼职信息不存在，无法发送拒绝通知，partTimeId: {}", one.getPartTimeId());
        }
    }

    @Override
    @Transactional
    public void applyPartTime(ApplyPartTime applyPartTime) {
        // 1. 验证兼职是否存在
        PartTimePO partTimePO = partTimeMapper.selectById(applyPartTime.getPartTimeId());
        Assert.notNull(partTimePO, "兼职不存在");

        // 2. 验证用户是否为发布者
        Long currentUserId = UserContext.getUser();
        if (Objects.equals(partTimePO.getPublisherId(), currentUserId)) {
            throw new RuntimeException("发布者不能申请自己的兼职");
        }

        // 3. 验证用户是否已经申请过该兼职
        ApplicationPO existingApplication = lambdaQuery()
                .eq(ApplicationPO::getPartTimeId, applyPartTime.getPartTimeId())
                .eq(ApplicationPO::getUserId, currentUserId)
                .one();
        if (existingApplication.getStatus() == 0 || existingApplication.getStatus() == 1) {
            throw new RuntimeException("已经申请过该兼职");
        }
        if(existingApplication.getStatus() == 2 || existingApplication.getStatus() == 3) {
            // 如果之前的申请被拒绝或取消，可以重新申请，先删除之前的记录
            // 如果更新时间是七天内的，不允许申请
            if(existingApplication.getUpdateTime() != null &&
                    existingApplication.getUpdateTime().isAfter(LocalDateTime.now().minusDays(7))) {
                throw new RuntimeException("之前的申请被拒绝或取消，七天内不能重新申请");
            }
            this.removeById(existingApplication.getId());
        }

        // 4. 创建申请记录
        ApplicationPO applicationPO = new ApplicationPO();
        applicationPO.setPartTimeId(applyPartTime.getPartTimeId());
        applicationPO.setTitle(partTimePO.getTitle());
        applicationPO.setUserId(currentUserId);
        applicationPO.setPublisherId(partTimePO.getPublisherId());
        applicationPO.setResume(applyPartTime.getResume());
        applicationPO.setRemark(applyPartTime.getRemark());
        applicationPO.setApplyTime(LocalDateTime.now());
        applicationPO.setStatus(0); // 0-待审核
        applicationPO.setCompanyName(applyPartTime.getUserName());
        applicationPO.setCompanyPhone(applyPartTime.getPhone());
        this.save(applicationPO);

        // 5. 更新兼职的申请人数
        partTimePO.setApplicantCount(partTimePO.getApplicantCount() + 1);
        partTimeMapper.updateById(partTimePO);

        // 发送消息通知发布者有新的申请
        applicationMessageProducer.sendNewApplicationMessage(
                applicationPO.getId(),
                applyPartTime.getPartTimeId(),
                partTimePO.getPublisherId(),
                partTimePO.getTitle());

        // 6. 清除相关缓存
        String detailCacheKey = "job:detail:" + applyPartTime.getPartTimeId();
        redisTemplate.delete(detailCacheKey);
        // 清除推荐列表缓存，因为申请人数变化可能影响推荐
        String recommendCacheKey = "job:list:recommend";
        redisTemplate.delete(recommendCacheKey);
        log.info("清除兼职缓存: detail={}, recommend={}", applyPartTime.getPartTimeId(), recommendCacheKey);
    }

}
