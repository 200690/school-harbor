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
//            获取item详情
            ItemPO itemPO = secondHandMapper.selectById(favoritePO.getItemId());
            favoriteVO.setItemTitle(itemPO.getTitle())
                    .setItemPrice(itemPO.getPrice())
                    .setItemCoverImage(itemPO.getCoverImage())
                    .setItemSchool(itemPO.getSchool())
                    .setItemLocation(itemPO.getLocation());
            return favoriteVO;
        }).toList();
        return new PageDTO<>( favoritePOS.getTotal(), favoritePOS.getPages(), favoriteVOS );
    }
}
