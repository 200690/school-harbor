package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.po.FavoritePO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.FavoriteVO;
import com.harbor.secondHand.mapper.FavoriteMapper;
import com.harbor.secondHand.mapper.SecondHandMapper;
import com.harbor.secondHand.service.IFavorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteImpl extends ServiceImpl<FavoriteMapper, FavoritePO> implements IFavorite {
    private final SecondHandMapper secondHandMapper;

    @Override
    public PageDTO<FavoriteVO> getMyFavorites(PageQuery pageQuery) {
        Page<FavoritePO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize() );

        Page<FavoritePO> favoritePOS = lambdaQuery()
                .eq(FavoritePO::getUserId, pageQuery.getId())
                .orderByDesc(FavoritePO::getCreateTime)  // 按创建时间倒序排序
                .page(page);
//        拷贝PO到VO
        List<FavoriteVO> favoriteVOS = favoritePOS.getRecords().stream().map(favoritePO -> {
            FavoriteVO favoriteVO = new FavoriteVO();
            BeanUtil.copyProperties(favoritePO, favoriteVO);
            ItemPO itemPO = secondHandMapper.selectById(favoritePO.getItemId());
            if (itemPO == null) {
                return null;
            }
            favoriteVO.setItemTitle(itemPO.getTitle())
                    .setItemPrice(itemPO.getPrice())
                    .setItemCoverImage(itemPO.getCoverImage())
                    .setItemSchool(itemPO.getSchool())
                    .setItemLocation(itemPO.getLocation());
            return favoriteVO;
        }).filter(vo -> vo != null).toList();
        return new PageDTO<>( favoritePOS.getTotal(), favoritePOS.getPages(), favoriteVOS );
    }

    @Override
    @Transactional
    public void removeFavorite(Long itemId) {
        lambdaUpdate()
                .eq(FavoritePO::getItemId, itemId)
                .remove();
        ItemPO itemPO = secondHandMapper.selectById(itemId);
        Assert.notNull(itemPO, "兼职任务没找到");
        if(itemPO.getFavoriteCount() > 0)
            itemPO.setFavoriteCount(itemPO.getFavoriteCount() - 1);
        secondHandMapper.updateById(itemPO);
    }

    @Override
    @Transactional
    public void addFavorite(Long itemId) {
        Long userId = com.harbor.common.utils.UserContext.getUser();
        // 检查是否已经收藏过
        boolean exists = lambdaQuery()
                .eq(FavoritePO::getUserId, userId)
                .eq(FavoritePO::getItemId, itemId)
                .exists();
        if (!exists) {
            // 创建收藏记录
            FavoritePO favoritePO = new FavoritePO();
            favoritePO.setUserId(userId);
            favoritePO.setItemId(itemId);
            this.save(favoritePO);
            
            // 更新商品收藏数
            ItemPO itemPO = secondHandMapper.selectById(itemId);
            Assert.notNull(itemPO, "商品没找到");
            itemPO.setFavoriteCount(itemPO.getFavoriteCount() + 1);
            secondHandMapper.updateById(itemPO);
        }
    }
}
