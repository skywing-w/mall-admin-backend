package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.dto.OrderQuery;
import com.mall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询（关联用户信息）。
     */
    IPage<Order> pageByQuery(IPage<Order> page, @Param("query") OrderQuery query);

    /**
     * 查询订单详情（含 items + user + address）。
     */
    Order selectDetail(Long id);

    /**
     * 获取每日销售额（date, total_amount）。
     */
    List<Map<String, Object>> dailySales(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 订单状态分布。
     */
    List<Map<String, Object>> statusDistribution();
}
