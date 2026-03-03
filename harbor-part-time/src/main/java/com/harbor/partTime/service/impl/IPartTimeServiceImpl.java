package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.common.utils.UserContext;
import com.harbor.partTime.domain.dto.PartTimeCreateDTO;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.po.FavoritePO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.BaseJobStatusVO;
import com.harbor.partTime.domain.vo.MyJobs;
import com.harbor.partTime.domain.vo.PartTimeDetailVO;
import com.harbor.partTime.domain.vo.PartTimeVO;
import com.harbor.partTime.mapper.ApplicationMapper;
import com.harbor.partTime.mapper.FavoriteMapper;
import com.harbor.partTime.mapper.PartTimeMapper;
import com.harbor.partTime.service.IPartTimeService;
import com.harbor.utils.client.UserClient;
import com.harbor.utils.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class IPartTimeServiceImpl extends ServiceImpl<PartTimeMapper, PartTimePO> implements IPartTimeService {
    private final ApplicationMapper applicationMapper;

    private final UserClient userClient;

    private final FavoriteMapper favoriteMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public PageDTO<PartTimeVO> queryPartTimeList(PartTimeQueryDTO dto) {
        // 对于首页推荐列表，尝试从缓存获取
        if (dto.getPage() == 1 && dto.getSize() == 10 && !StringUtils.hasText(dto.getKeyword()) &&
                (dto.getTypes() == null || dto.getTypes().isEmpty()) && dto.getCreditScore() == null) {
            String cacheKey = "job:list:recommend";
            PageDTO<PartTimeVO> cachedList = (PageDTO<PartTimeVO>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedList != null) {
                log.info("从缓存获取推荐兼职列表");
                return cachedList;
            }
        }

        // 1. 构建分页参数
        Page<PartTimePO> page = new Page<>(dto.getPage(), dto.getSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<PartTimePO> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（标题或描述）
        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w
                    .like(PartTimePO::getTitle, dto.getKeyword())
                    .or()
                    .like(PartTimePO::getDescription, dto.getKeyword()));
        }

        wrapper.eq(PartTimePO::getStatus, 1);

        // 兼职类型筛选（支持多选）
        if (dto.getTypes() != null && !dto.getTypes().isEmpty()) {
            wrapper.in(PartTimePO::getType, dto.getTypes());
        }

        // 信誉分范围筛选
        if (dto.getCreditScore() != null) {
            // 根据信誉分范围进行筛选
            int creditScore = dto.getCreditScore();
            if (creditScore >= 81) {
                wrapper.ge(PartTimePO::getCreditScore, 81); // 优秀
            } else if (creditScore >= 61) {
                wrapper.between(PartTimePO::getCreditScore, 61, 80); // 中等
            } else {
                wrapper.le(PartTimePO::getCreditScore, 60); // 差
            }
        }

        // 3. 排序处理
        if (StringUtils.hasText(dto.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(dto.getSortOrder());

            switch (dto.getSortField()) {
                case "publishTime":
                    wrapper.orderBy(true, isAsc, PartTimePO::getPublishTime);
                    break;
                case "viewCount":
                    wrapper.orderBy(true, isAsc, PartTimePO::getViewCount);
                    break;
                case "applicantCount":
                    wrapper.orderBy(true, isAsc, PartTimePO::getApplicantCount);
                    break;
                case "creditScore":
                    wrapper.orderBy(true, isAsc, PartTimePO::getCreditScore);
                    break;
                default:
                    // 默认按发布时间降序
                    wrapper.orderByDesc(PartTimePO::getPublishTime);
            }
        } else {
            // 默认排序：按发布时间降序
            wrapper.orderByDesc(PartTimePO::getPublishTime);
        }

        // 4. 执行分页查询
        Page<PartTimePO> partTimePage = this.page(page, wrapper);
        // 填入BaseJobStatusVO
        List<PartTimeVO> voList = partTimePage.getRecords().stream().map(partTimePO -> {
            PartTimeVO partTimeVO = new PartTimeVO();
            BeanUtil.copyProperties(partTimePO, partTimeVO, CopyOptions.create().ignoreNullValue());

            this.setBaseJobStatusVO(partTimeVO, partTimeVO.getId(), partTimePO);
            return partTimeVO;
        }).toList();

        PageDTO<PartTimeVO> result = new PageDTO<>(partTimePage.getTotal(), partTimePage.getPages(), voList);

        // 缓存推荐列表，设置10分钟过期
        if (dto.getPage() == 1 && dto.getSize() == 10 && !StringUtils.hasText(dto.getKeyword()) &&
                (dto.getTypes() == null || dto.getTypes().isEmpty()) && dto.getCreditScore() == null) {
            String cacheKey = "job:list:recommend";
            redisTemplate.opsForValue().set(cacheKey, result, 10, java.util.concurrent.TimeUnit.MINUTES);
            log.info("缓存推荐兼职列表");
        }

        // 5. 转换为VO并返回
        return result;
    }

    /**
     * 发布兼职
     * 
     * @param partTimeCreateDTO
     */
    @Override
    public void newJob(PartTimeCreateDTO partTimeCreateDTO) {
        PartTimePO partTimePO = new PartTimePO();
        partTimePO.setPublisherId(UserContext.getUser());
        BeanUtil.copyProperties(partTimeCreateDTO, partTimePO);
        this.save(partTimePO);
    }

    /**
     * 获取我发布的兼职列表
     *
     * @param pageQuery
     * @return
     */
    public PageDTO<MyJobs> getMyJobs(PageQuery pageQuery) {
        log.info("获取我发布的兼职列表");

        if (pageQuery.getId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        // 1. 创建分页对象（当前页，每页大小）
        Page<PartTimePO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        // 2. 执行分页查询
        Page<PartTimePO> pageResult = lambdaQuery()
                .eq(PartTimePO::getPublisherId, pageQuery.getId())
                .orderByDesc(PartTimePO::getPublishTime) // 按发布时间倒序排序
                .page(page);

        // 3. 转换数据
        List<MyJobs> records = pageResult.getRecords().stream()
                .map(partTimePO -> BeanUtil.copyProperties(partTimePO, MyJobs.class))
                .toList();

        // 4. 封装返回结果

        return new PageDTO<>(pageResult.getTotal(), pageResult.getPages(), records);
    }

    /**
     * 修改兼职状态
     * 
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        PartTimePO partTimePO = lambdaQuery().eq(PartTimePO::getId, id).one();
        Assert.notNull(partTimePO, "兼职不存在");
        if (partTimePO.getStatus() == status) {
            return;
        }
        if (partTimePO.getStatus() > 1) {
            throw new RuntimeException("兼职状态异常");
        }
        partTimePO.setStatus(status);
        this.updateById(partTimePO);

        // 清除缓存
        String detailCacheKey = "job:detail:" + id;
        redisTemplate.delete(detailCacheKey);
        // 清除推荐列表缓存，因为状态变化可能影响推荐
        String recommendCacheKey = "job:list:recommend";
        redisTemplate.delete(recommendCacheKey);
        log.info("清除兼职缓存: detail={}, recommend={}", id, recommendCacheKey);
    }

    /**
     * 修改兼职信息
     * 
     * @param partTimeDTO
     */
    @Override
    public void updateById(PartTimeCreateDTO partTimeDTO) {
        PartTimePO partTimePO = lambdaQuery().eq(PartTimePO::getId, partTimeDTO.getId()).one();
        Assert.notNull(partTimePO, "兼职不存在");
        BeanUtil.copyProperties(partTimeDTO, partTimePO, CopyOptions.create().ignoreNullValue());
        partTimePO.setUpdateTime(LocalDateTime.now());
        this.updateById(partTimePO);

        // 清除缓存
        String detailCacheKey = "job:detail:" + partTimeDTO.getId();
        redisTemplate.delete(detailCacheKey);
        // 清除推荐列表缓存，因为信息变化可能影响推荐
        String recommendCacheKey = "job:list:recommend";
        redisTemplate.delete(recommendCacheKey);
        log.info("清除兼职缓存: detail={}, recommend={}", partTimeDTO.getId(), recommendCacheKey);
    }

    /**
     * 获取兼职详情
     * 
     * @param partTimeId
     * @return
     */
    @Override
    public PartTimeDetailVO getJobById(Long partTimeId) {
        Assert.notNull(partTimeId, "兼职ID不能为空");

        // 尝试从缓存获取
        String cacheKey = "job:detail:" + partTimeId;
        PartTimeDetailVO cachedDetail = (PartTimeDetailVO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedDetail != null) {
            log.info("从缓存获取兼职详情: {}", partTimeId);
            return cachedDetail;
        }

        // 浏览量++
        PartTimePO partTimePO = lambdaQuery().eq(PartTimePO::getId, partTimeId).one();
        partTimePO.setViewCount(partTimePO.getViewCount() + 1);
        this.updateById(partTimePO);

        // 封装BaseJobStatusVO
        PartTimeDetailVO partTimeDetailVO = new PartTimeDetailVO();
        Assert.notNull(partTimePO, "兼职不存在");
        BeanUtil.copyProperties(partTimePO, partTimeDetailVO);
        this.setBaseJobStatusVO(partTimeDetailVO, partTimeId, partTimePO);

        // 封装user部分
        log.info("获取用户信息,{}", partTimePO.getPublisherId());
        Result<UserInfoDTO> userInfoDTOResult = userClient.info(partTimePO.getPublisherId());
        partTimeDetailVO.setUsername(userInfoDTOResult.getData().getUsername());
        partTimeDetailVO.setImg(userInfoDTOResult.getData().getImg());

        // 缓存结果，设置1小时过期
        redisTemplate.opsForValue().set(cacheKey, partTimeDetailVO, 1, java.util.concurrent.TimeUnit.HOURS);
        log.info("缓存兼职详情: {}", partTimeId);

        return partTimeDetailVO;
    }

    @Override
    public List<PartTimePO> getJobsById(List<Long> ids) {
        Assert.notEmpty(ids, "ids不能为空");
        return lambdaQuery().in(PartTimePO::getId, ids).list();
    }

    public <T extends BaseJobStatusVO> void setBaseJobStatusVO(T vo, Long partTimeId, PartTimePO po) {
        vo.setIsPublisher(Objects.equals(po.getPublisherId(), UserContext.getUser()));
        if (vo.getIsPublisher() == true) {
            vo.setApplicable(false);
        } else {
            ApplicationPO one = applicationMapper.selectOne(new LambdaQueryWrapper<ApplicationPO>()
                    .eq(ApplicationPO::getPartTimeId, partTimeId).eq(ApplicationPO::getUserId, UserContext.getUser()));
            vo.setApplicable(one == null);
        }
        FavoritePO favoritePO = favoriteMapper.selectOne(new LambdaQueryWrapper<FavoritePO>()
                .eq(FavoritePO::getPartTimeId, partTimeId).eq(FavoritePO::getUserId, UserContext.getUser()));
        vo.setIsFavorite(favoritePO != null);
    }
}
