package com.mall.controller;

import com.mall.common.Result;
import com.mall.service.StatsService;
import com.mall.vo.OverviewVO;
import com.mall.vo.SalesTrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    //首页数据概览
    @GetMapping("/overview")
    public Result<OverviewVO> overview() {
        return Result.success(statsService.overview());
    }

    //销售趋势
    @GetMapping("/sales-trend")
    public Result<List<SalesTrendVO>> salesTrend(@RequestParam(required = false) String startDate,
                                                 @RequestParam(required = false) String endDate) {
        return Result.success(statsService.salesTrend(startDate, endDate));
    }
}
