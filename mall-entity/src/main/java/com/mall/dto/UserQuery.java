package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends PageQuery {
    private String name;
    private String username;
    private String phone;
    private Long deptId;
    private Integer status;
}
