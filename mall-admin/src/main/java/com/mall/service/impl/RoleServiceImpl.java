package com.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.RoleQuery;
import com.mall.entity.Role;
import com.mall.mapper.RoleMapper;
import com.mall.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public PageResult<Role> page(RoleQuery query) {
        IPage<Role> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getName()), Role::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getLabel()), Role::getLabel, query.getLabel())
                .orderByDesc(Role::getId)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }
}
