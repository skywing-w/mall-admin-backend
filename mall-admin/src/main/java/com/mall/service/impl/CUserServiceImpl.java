package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.CUserQuery;
import com.mall.entity.CUser;
import com.mall.mapper.CUserMapper;
import com.mall.service.CUserService;
import org.springframework.stereotype.Service;

@Service
public class CUserServiceImpl extends ServiceImpl<CUserMapper, CUser> implements CUserService {

    @Override
    public PageResult<CUser> page(CUserQuery query) {
        IPage<CUser> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getNickname()), CUser::getNickname, query.getNickname())
                .like(StrUtil.isNotBlank(query.getPhone()), CUser::getPhone, query.getPhone())
                .eq(query.getStatus() != null, CUser::getStatus, query.getStatus())
                .orderByDesc(CUser::getId)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }
}
