package com.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.CartItem;

import java.util.List;

public interface CartItemMapper extends BaseMapper<CartItem> {
    IPage<CartItem> pageWithProduct(IPage<CartItem> page);
    List<CartItem> selectWithProduct();
}
