package com.mall.service.impl;

import com.mall.mapper.StatsMapper;
import com.mall.service.StatsService;
import com.mall.vo.OverviewVO;
import com.mall.vo.SalesTrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statsMapper;

    @Override
    public OverviewVO overview() {
        return statsMapper.overview();
    }

    @Override
    public List<SalesTrendVO> salesTrend(String startDate, String endDate) {
        return statsMapper.salesTrend(startDate, endDate);
    }
}
