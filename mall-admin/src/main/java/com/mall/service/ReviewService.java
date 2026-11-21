package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.ReviewQuery;
import com.mall.entity.Review;

public interface ReviewService extends IService<Review> {
    PageResult<Review> page(ReviewQuery query);
}
