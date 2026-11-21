package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQuery extends PageQuery {
    private String orderNo;
    private String phone;
    private Integer status;
    /** 起始日期（含），格式 yyyy-MM-dd */
    private String startDate;
    /** 结束日期（含） */
    private String endDate;
}
