package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.CouponQuery;
import com.mall.entity.Coupon;
import com.mall.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public Result<PageResult<Coupon>> page(CouponQuery query) {
        return Result.success(couponService.page(query));
    }

    @GetMapping("/{id}")
    public Result<Coupon> getById(@PathVariable Long id) {
        return Result.success(couponService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "优惠券", type = "新增")
    public Result<Void> add(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "优惠券", type = "修改")
    public Result<Void> update(@RequestBody Coupon coupon) {
        couponService.updateById(coupon);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "优惠券", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        couponService.removeById(id);
        return Result.success();
    }
}
