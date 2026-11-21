package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Order;
import com.mall.vo.OverviewVO;
import com.mall.vo.SalesTrendVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatsMapper extends BaseMapper<Order> {
    OverviewVO overview();

    List<SalesTrendVO> salesTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
