package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.CategoryPO;
import com.harbor.secondHand.mapper.Category;
import com.harbor.secondHand.service.ICategory;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<Category, CategoryPO> implements ICategory {
}
