package com.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.ProductQuery;
import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public PageResult<Product> page(ProductQuery query) {
        IPage<Product> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<Product> res = baseMapper.pageByQuery(page, query);
        return PageResult.of(res.getTotal(), res.getRecords());
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Product p = new Product();
        p.setId(id);
        p.setStatus(status);
        return updateById(p);
    }
}
