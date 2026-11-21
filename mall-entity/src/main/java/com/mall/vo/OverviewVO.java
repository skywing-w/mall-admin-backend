package com.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页数据概览。
 */
@Data
public class OverviewVO {
    private BigDecimal todaySales;
    private Long todayOrders;
    private Long todayNewUsers;
    private Long pendingShipOrders;
    private Long totalOrders;
    private Long totalUsers;
    private BigDecimal totalSales;
    private Long onSaleProducts;
}
