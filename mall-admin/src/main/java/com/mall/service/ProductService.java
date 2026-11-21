package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.ProductQuery;
import com.mall.entity.Product;

public interface ProductService extends IService<Product> {
    PageResult<Product> page(ProductQuery query);
    boolean updateStatus(Long id, Integer status);
}
