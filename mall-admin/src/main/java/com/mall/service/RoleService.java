package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.RoleQuery;
import com.mall.entity.Role;

public interface RoleService extends IService<Role> {
    PageResult<Role> page(RoleQuery query);
}
