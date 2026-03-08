package com.harbor.secondHand.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.user.domain.po.UserBlockProductPO;
import com.harbor.secondHand.user.domain.vo.BlockProductDetailVO;
import com.harbor.secondHand.user.mapper.UserBlockProductMapper;
import com.harbor.secondHand.user.service.IBlockProductService;
import com.harbor.utils.client.SecondHandClient;
import com.harbor.utils.dto.ItemMainDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.startup.UserConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ZLL15
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BlockProductServiceImpl extends ServiceImpl<UserBlockProductMapper, UserBlockProductPO> implements IBlockProductService {
    private final SecondHandClient secondHandClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageDTO<BlockProductDetailVO> getMyBlockProducts(PageQuery pageQuery) {
        // 生成缓存key（存储所有拉黑的商品id集合）
        String cacheKey = "user:block:product:ids:" + pageQuery.getId();
        
        // 尝试从缓存获取所有拉黑的商品id集合
        List<Long> productIds = (List<Long>) redisTemplate.opsForValue().get(cacheKey);
        
        // 缓存不存在，从数据库查询所有拉黑的商品id
        if (productIds == null) {
            List<UserBlockProductPO> blockProducts = lambdaQuery()
                    .eq(UserBlockProductPO::getUserId, pageQuery.getId())
                    .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                    .list();
            productIds = blockProducts.stream()
                    .map(UserBlockProductPO::getProductId)
                    .collect(java.util.stream.Collectors.toList());
            
            // 写入缓存，设置1天过期
            redisTemplate.opsForValue().set(cacheKey, productIds, 1, TimeUnit.DAYS);
            log.info("缓存拉黑商品id集合: userId={}, size={}", pageQuery.getId(), productIds.size());
        }
        
        // 构建分页查询
        Page<UserBlockProductPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<UserBlockProductPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBlockProductPO::getUserId, pageQuery.getId())
                .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                .orderByDesc(UserBlockProductPO::getBlockTime);

        Page<UserBlockProductPO> result = this.page(page, wrapper);
        if(result.getTotal() == 0)
            return null;

        List<BlockProductDetailVO> blockProductDetailVOS = result.getRecords().stream().map(po -> {
            BlockProductDetailVO productDetailVO = new BlockProductDetailVO();
            BeanUtil.copyProperties(po, productDetailVO);
            ItemMainDTO itemMainDTO = secondHandClient.getItemInfo(po.getProductId()).getData();
            BeanUtil.copyProperties(itemMainDTO, productDetailVO);
            return productDetailVO;
        }).toList();
        
        PageDTO<BlockProductDetailVO> pageResult = new PageDTO<>( result.getTotal(), result.getPages(), blockProductDetailVOS);
        
        log.info("获取用户屏蔽的商品列表成功,{}", blockProductDetailVOS);
        return pageResult;
    }

    @Override
    public void removeByItemId(Long Itemid) {
        Assert.notNull(Itemid, "商品ID不能为空");
        Long userId = UserContext.getUser();
        
        lambdaUpdate().eq(UserBlockProductPO::getProductId, Itemid)
                .eq(UserBlockProductPO::getUserId, userId)
                .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                .set(UserBlockProductPO::getStats, UserBlockProductPO.STATS_INACTIVE)
                .update();
        
        // 清除并重新写入当前用户的缓存
        String cacheKey = "user:block:product:ids:" + userId;
        
        // 清除缓存
        redisTemplate.delete(cacheKey);
        log.info("清除用户拉黑商品id集合缓存: userId={}", userId);
        
        // 重新查询并写入缓存
        List<UserBlockProductPO> updatedBlockProducts = lambdaQuery()
                .eq(UserBlockProductPO::getUserId, userId)
                .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                .list();
        List<Long> productIds = updatedBlockProducts.stream()
                .map(UserBlockProductPO::getProductId)
                .collect(java.util.stream.Collectors.toList());
        
        // 写入缓存，设置1天过期
        redisTemplate.opsForValue().set(cacheKey, productIds, 1, java.util.concurrent.TimeUnit.DAYS);
        log.info("重新缓存拉黑商品id集合: userId={}, size={}", userId, productIds.size());
        
        // 清除当前用户的二手交易缓存
        String secondHandCacheKey = "item:list:latest:" + userId;
        redisTemplate.delete(secondHandCacheKey);
        log.info("清除二手交易缓存: key={}", secondHandCacheKey);
    }

    @Override
    public void addBlock(Long itemId) {
        Assert.notNull(itemId, "商品ID不能为空");
        Long userId = UserContext.getUser();
        
        // 检查是否存在该用户对该商品的任何记录（无论状态）
        UserBlockProductPO existingRecord = lambdaQuery()
                .eq(UserBlockProductPO::getUserId, userId)
                .eq(UserBlockProductPO::getProductId, itemId)
                .one();
        
        if (existingRecord != null) {
            // 如果记录存在，检查状态
            if (existingRecord.getStats() == UserBlockProductPO.STATS_ACTIVE) {
                log.info("商品已经被拉黑: userId={}, itemId={}", userId, itemId);
                return;
            } else {
                // 如果记录存在但状态为非活跃，更新为活跃
                lambdaUpdate()
                        .eq(UserBlockProductPO::getUserId, userId)
                        .eq(UserBlockProductPO::getProductId, itemId)
                        .set(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                        .set(UserBlockProductPO::getBlockTime, java.time.LocalDateTime.now())
                        .update();
                log.info("重新拉黑商品: userId={}, itemId={}", userId, itemId);
            }
        } else {
            // 如果记录不存在，创建新记录
            UserBlockProductPO blockProductPO = new UserBlockProductPO();
            blockProductPO.setUserId(userId);
            blockProductPO.setProductId(itemId);
            blockProductPO.setBlockTime(java.time.LocalDateTime.now());
            blockProductPO.setStats(UserBlockProductPO.STATS_ACTIVE);
            this.save(blockProductPO);
            log.info("添加拉黑商品: userId={}, itemId={}", userId, itemId);
        }
        
        // 清除并更新缓存
        String cacheKey = "user:block:product:ids:" + userId;
        redisTemplate.delete(cacheKey);
        
        // 重新查询并写入缓存
        List<UserBlockProductPO> updatedBlockProducts = lambdaQuery()
                .eq(UserBlockProductPO::getUserId, userId)
                .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                .list();
        List<Long> productIds = updatedBlockProducts.stream()
                .map(UserBlockProductPO::getProductId)
                .collect(java.util.stream.Collectors.toList());
        
        // 写入缓存，设置1天过期
        redisTemplate.opsForValue().set(cacheKey, productIds, 1, java.util.concurrent.TimeUnit.DAYS);
        log.info("更新拉黑商品id集合缓存: userId={}, size={}", userId, productIds.size());
        
        // 清除当前用户的二手交易缓存
        String secondHandCacheKey = "item:list:latest:" + userId;
        redisTemplate.delete(secondHandCacheKey);
        log.info("清除二手交易缓存: key={}", secondHandCacheKey);
    }
}
