package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.common.PageQuery;
import com.mall.entity.CartItem;

public interface CartItemService extends IService<CartItem> {
    PageResult<CartItem> page(PageQuery query);
}
