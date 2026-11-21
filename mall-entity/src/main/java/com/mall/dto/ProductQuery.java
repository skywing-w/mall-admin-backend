package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductQuery extends PageQuery {
    private String name;
    private Long categoryId;
    private Integer status;
}
