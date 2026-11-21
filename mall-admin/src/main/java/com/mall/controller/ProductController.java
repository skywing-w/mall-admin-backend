package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.ProductQuery;
import com.mall.entity.Product;
import com.mall.exception.BusinessException;
import com.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Result<PageResult<Product>> page(ProductQuery query) {
        return Result.success(productService.page(query));
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "商品", type = "新增")
    public Result<Void> add(@RequestBody Product product) {
        if (product.getName() == null || product.getCategoryId() == null) {
            throw new BusinessException("商品名和分类不能为空");
        }
        productService.save(product);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "商品", type = "修改")
    public Result<Void> update(@RequestBody Product product) {
        if (product.getId() == null) throw new BusinessException("id 不能为空");
        productService.updateById(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "商品", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}/{status}")
    @LogOperation(module = "商品", type = "上下架")
    public Result<Void> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.success();
    }
}
