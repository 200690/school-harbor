package com.harbor.secondHand.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.po.UserBlockProductPO;
import com.harbor.secondHand.user.domain.vo.BlockProductDetailVO;
import com.harbor.secondHand.user.mapper.UserBlockProductMapper;
import com.harbor.secondHand.user.service.IBlockProductService;
import com.harbor.utils.client.SecondHandClient;
import com.harbor.utils.dto.ItemMainDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZLL15
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BlockProductServiceImpl extends ServiceImpl<UserBlockProductMapper, UserBlockProductPO> implements IBlockProductService {
    private final SecondHandClient secondHandClient;

    @Override
    public PageDTO<BlockProductDetailVO> getMyBlockProducts(PageQuery pageQuery) {
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
        log.info("获取用户屏蔽的商品列表成功,{}", blockProductDetailVOS);
        return new PageDTO<>( result.getTotal(), result.getPages(), blockProductDetailVOS);
    }

    @Override
    public void removeByItemId(Long Itemid) {
        Assert.notNull(Itemid, "商品ID不能为空");
        lambdaUpdate().eq(UserBlockProductPO::getProductId, Itemid)
                .eq(UserBlockProductPO::getStats, UserBlockProductPO.STATS_ACTIVE)
                .set(UserBlockProductPO::getStats, UserBlockProductPO.STATS_INACTIVE)
                .update();
    }
}
