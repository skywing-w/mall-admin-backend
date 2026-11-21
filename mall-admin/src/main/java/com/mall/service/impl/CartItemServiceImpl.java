package com.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageQuery;
import com.mall.common.PageResult;
import com.mall.entity.CartItem;
import com.mall.mapper.CartItemMapper;
import com.mall.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Override
    public PageResult<CartItem> page(PageQuery query) {
        IPage<CartItem> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<CartItem> res = baseMapper.pageWithProduct(page);
        return PageResult.of(res.getTotal(), res.getRecords());
    }
}
