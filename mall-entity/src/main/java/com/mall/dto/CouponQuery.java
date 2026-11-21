package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CouponQuery extends PageQuery {
    private String name;
    private Integer type;
    private Integer status;
}
