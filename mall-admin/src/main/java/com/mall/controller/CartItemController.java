package com.mall.controller;

import com.mall.common.PageQuery;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.CartItem;
import com.mall.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    public Result<PageResult<CartItem>> page(PageQuery query) {
        return Result.success(cartItemService.page(query));
    }
}
