package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.CUserQuery;
import com.mall.entity.CUser;
import com.mall.service.CUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/c-users")
@RequiredArgsConstructor
public class CUserController {

    private final CUserService cUserService;

    @GetMapping
    public Result<PageResult<CUser>> page(CUserQuery query) {
        return Result.success(cUserService.page(query));
    }

    @GetMapping("/{id}")
    public Result<CUser> getById(@PathVariable Long id) {
        return Result.success(cUserService.getById(id));
    }
}
