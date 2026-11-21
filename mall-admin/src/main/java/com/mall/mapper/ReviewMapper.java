package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall.dto.ReviewQuery;
import com.mall.entity.Review;
import org.apache.ibatis.annotations.Param;

public interface ReviewMapper extends BaseMapper<Review> {
    IPage<Review> pageByQuery(IPage<Review> page, @Param("query") ReviewQuery query);
}
