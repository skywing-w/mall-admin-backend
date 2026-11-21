package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> selectAllEnabled();
}
