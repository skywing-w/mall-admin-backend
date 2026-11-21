package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.CategoryQuery;
import com.mall.entity.Category;
import com.mall.exception.BusinessException;
import com.mall.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Result<PageResult<Category>> page(CategoryQuery query) {
        return Result.success(categoryService.page(query));
    }

    @GetMapping("/all")
    public Result<List<Category>> all() {
        return Result.success(categoryService.list());
    }

    @GetMapping("/tree")
    public Result<List<Category>> tree() {
        return Result.success(categoryService.tree());
    }

    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "商品分类", type = "新增")
    public Result<Void> add(@RequestBody Category category) {
        if (category.getName() == null) throw new BusinessException("分类名称不能为空");
        if (category.getParentId() == null) category.setParentId(0L);
        categoryService.save(category);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "商品分类", type = "修改")
    public Result<Void> update(@RequestBody Category category) {
        if (category.getId() == null) throw new BusinessException("id 不能为空");
        categoryService.updateById(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "商品分类", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }
}
