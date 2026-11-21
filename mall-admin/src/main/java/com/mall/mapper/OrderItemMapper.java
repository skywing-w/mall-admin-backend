package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.OrderItem;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderItem> selectByOrderId(Long orderId);
}
