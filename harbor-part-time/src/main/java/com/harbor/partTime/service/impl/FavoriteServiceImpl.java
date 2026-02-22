package com.harbor.partTime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.partTime.domain.po.FavoritePO;
import com.harbor.partTime.mapper.FavoriteMapper;
import com.harbor.partTime.service.IFavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoritePO> implements IFavoriteService {
}
