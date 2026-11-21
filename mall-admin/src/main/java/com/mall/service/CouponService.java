package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.CouponQuery;
import com.mall.entity.Coupon;

public interface CouponService extends IService<Coupon> {
    PageResult<Coupon> page(CouponQuery query);
}
