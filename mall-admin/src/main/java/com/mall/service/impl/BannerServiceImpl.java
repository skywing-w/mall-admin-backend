package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.BannerQuery;
import com.mall.entity.Banner;
import com.mall.mapper.BannerMapper;
import com.mall.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public PageResult<Banner> page(BannerQuery query) {
        IPage<Banner> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getTitle()), Banner::getTitle, query.getTitle())
                .eq(query.getStatus() != null, Banner::getStatus, query.getStatus())
                .orderByAsc(Banner::getSort)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public List<Banner> allEnabled() {
        return baseMapper.selectAllEnabled();
    }
}
