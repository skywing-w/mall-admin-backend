package com.mall.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果封装。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private Long total;
    private List<T> rows;

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L, Collections.emptyList());
    }

    public static <T> PageResult<T> of(Long total, List<T> rows) {
        PageResult<T> p = new PageResult<>();
        p.setTotal(total);
        p.setRows(rows);
        return p;
    }
}
