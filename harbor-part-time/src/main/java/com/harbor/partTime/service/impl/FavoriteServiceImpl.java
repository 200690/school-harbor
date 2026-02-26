package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.partTime.domain.po.FavoritePO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.FavoriteVO;
import com.harbor.partTime.mapper.FavoriteMapper;
import com.harbor.partTime.mapper.PartTimeMapper;
import com.harbor.partTime.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoritePO> implements IFavoriteService {
    private final PartTimeMapper partTimeMapper;

    @Override
    public PageDTO<FavoriteVO> getMyFavorites(PageQuery pageQuery) {
        Page<FavoritePO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<FavoritePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoritePO::getUserId, pageQuery.getId());
        queryWrapper.orderByDesc(FavoritePO::getCreateTime);

        Page<FavoritePO> favoriteVOPage = this.page(page, queryWrapper);
        if(favoriteVOPage.getTotal() == 0){
            return PageDTO.empty(favoriteVOPage);
        }
        List<FavoriteVO> favoriteVOS = favoriteVOPage.getRecords().stream().map(FavoritePO -> {
//            拷贝PO
            FavoriteVO favoriteVO = new FavoriteVO();
            favoriteVO.setFavoriteId(FavoritePO.getId())
                    .setFavoriteTime(FavoritePO.getCreateTime())
                    .setPartTimeId(FavoritePO.getPartTimeId());
//            获取partTime
            PartTimePO partTimePO = partTimeMapper.selectById(FavoritePO.getPartTimeId());
            BeanUtil.copyProperties(partTimePO, favoriteVO, CopyOptions.create().ignoreNullValue());
            return favoriteVO;
        }).toList();

        return new PageDTO<>( favoriteVOPage.getTotal(), favoriteVOPage.getPages(), favoriteVOS);
    }

    @Override
    public void removeFavorite(Long partTimeId) {
        lambdaUpdate()
                .eq(FavoritePO::getPartTimeId, partTimeId)
                .remove();
    }
}
