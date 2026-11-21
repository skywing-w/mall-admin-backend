package com.mall.dto;

import com.mall.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CUserQuery extends PageQuery {
    private String nickname;
    private String phone;
    private Integer status;
}
