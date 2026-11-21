package com.mall.common;

import lombok.Data;

/**
 * 通用分页查询参数。
 */
@Data
public class PageQuery {

    /** 当前页（从 1 开始） */
    private Long page = 1L;

    /** 每页大小 */
    private Long pageSize = 10L;
}
