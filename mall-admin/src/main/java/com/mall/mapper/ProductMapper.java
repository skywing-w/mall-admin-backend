package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.dto.ProductQuery;
import com.mall.entity.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper extends BaseMapper<Product> {
    IPage<Product> pageByQuery(IPage<Product> page, @Param("query") ProductQuery query);
}
