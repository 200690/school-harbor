package com.harbor.secondHand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.po.FavoritePO;
import com.harbor.secondHand.domain.vo.FavoriteVO;

public interface IFavorite extends IService<FavoritePO> {
    PageDTO<FavoriteVO> getMyFavorites(PageQuery pageQuery);

    void removeFavorite(Long itemId);

    void addFavorite(Long itemId);
}
