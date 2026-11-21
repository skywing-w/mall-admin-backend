package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQuery extends PageQuery {
    private String name;
    private Long parentId;
}
