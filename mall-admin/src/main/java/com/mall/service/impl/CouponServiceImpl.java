package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.CouponQuery;
import com.mall.entity.Coupon;
import com.mall.mapper.CouponMapper;
import com.mall.service.CouponService;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public PageResult<Coupon> page(CouponQuery query) {
        IPage<Coupon> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getName()), Coupon::getName, query.getName())
                .eq(query.getType() != null, Coupon::getType, query.getType())
                .eq(query.getStatus() != null, Coupon::getStatus, query.getStatus())
                .orderByDesc(Coupon::getId)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }
}
