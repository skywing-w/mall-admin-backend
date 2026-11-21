package com.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 每日销售趋势。
 */
@Data
public class SalesTrendVO {
    private String date;
    private BigDecimal amount;
    private Long count;
}
