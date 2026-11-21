package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.BannerQuery;
import com.mall.entity.Banner;
import com.mall.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public Result<PageResult<Banner>> page(BannerQuery query) {
        return Result.success(bannerService.page(query));
    }

    @GetMapping("/all")
    public Result<List<Banner>> allEnabled() {
        return Result.success(bannerService.allEnabled());
    }

    @GetMapping("/{id}")
    public Result<Banner> getById(@PathVariable Long id) {
        return Result.success(bannerService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "轮播图", type = "新增")
    public Result<Void> add(@RequestBody Banner banner) {
        bannerService.save(banner);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "轮播图", type = "修改")
    public Result<Void> update(@RequestBody Banner banner) {
        bannerService.updateById(banner);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "轮播图", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }
}
