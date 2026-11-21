package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.CategoryQuery;
import com.mall.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    PageResult<Category> page(CategoryQuery query);
    List<Category> tree();
}
