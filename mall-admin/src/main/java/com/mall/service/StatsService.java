package com.mall.service;

import com.mall.vo.OverviewVO;
import com.mall.vo.SalesTrendVO;

import java.util.List;

public interface StatsService {
    OverviewVO overview();

    List<SalesTrendVO> salesTrend(String startDate, String endDate);
}
