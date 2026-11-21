package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.OrderQuery;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Result<PageResult<Order>> page(OrderQuery query) {
        return Result.success(orderService.page(query));
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        return Result.success(orderService.detail(id));
    }

    @PutMapping("/ship/{id}")
    @LogOperation(module = "订单", type = "发货")
    public Result ship(@PathVariable Long id) {
        orderService.ship(id);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    @LogOperation(module = "订单", type = "取消")
    public Result cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success();
    }

    @PutMapping("/refund/{id}")
    @LogOperation(module = "订单", type = "退款")
    public Result refund(@PathVariable Long id) {
        orderService.refund(id);
        return Result.success();
    }
}
