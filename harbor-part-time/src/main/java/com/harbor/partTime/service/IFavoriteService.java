package com.harbor.partTime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.partTime.domain.po.FavoritePO;
import com.harbor.partTime.domain.vo.FavoriteVO;

public interface IFavoriteService extends IService<FavoritePO> {
    PageDTO<FavoriteVO> getMyFavorites(PageQuery pageQuery);

    void removeFavorite(Long partTimeId);
}
