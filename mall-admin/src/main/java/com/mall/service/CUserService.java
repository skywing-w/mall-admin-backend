package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.CUserQuery;
import com.mall.entity.CUser;

public interface CUserService extends IService<CUser> {
    PageResult<CUser> page(CUserQuery query);
}
