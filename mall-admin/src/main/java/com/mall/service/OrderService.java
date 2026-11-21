package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.OrderQuery;
import com.mall.entity.Order;

public interface OrderService extends IService<Order> {
    PageResult<Order> page(OrderQuery query);
    Order detail(Long id);
    boolean ship(Long id);
    boolean cancel(Long id);
    boolean refund(Long id);
}
