package com.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.ReviewQuery;
import com.mall.entity.Review;
import com.mall.mapper.ReviewMapper;
import com.mall.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public PageResult<Review> page(ReviewQuery query) {
        IPage<Review> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<Review> res = baseMapper.pageByQuery(page, query);
        return PageResult.of(res.getTotal(), res.getRecords());
    }
}
