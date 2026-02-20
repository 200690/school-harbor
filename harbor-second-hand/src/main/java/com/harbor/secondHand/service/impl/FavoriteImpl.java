package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.FavoritePO;
import com.harbor.secondHand.mapper.Favorite;
import com.harbor.secondHand.service.IFavorite;
import org.springframework.stereotype.Service;

@Service
public class FavoriteImpl extends ServiceImpl<Favorite, FavoritePO> implements IFavorite {
}
