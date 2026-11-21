package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.ReviewQuery;
import com.mall.entity.Review;
import com.mall.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public Result<PageResult<Review>> page(ReviewQuery query) {
        return Result.success(reviewService.page(query));
    }

    @GetMapping("/{id}")
    public Result<Review> getById(@PathVariable Long id) {
        return Result.success(reviewService.getById(id));
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "评价", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        reviewService.removeById(id);
        return Result.success();
    }
}
