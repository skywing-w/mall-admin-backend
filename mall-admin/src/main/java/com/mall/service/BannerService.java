package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.BannerQuery;
import com.mall.entity.Banner;

import java.util.List;

public interface BannerService extends IService<Banner> {
    PageResult<Banner> page(BannerQuery query);
    List<Banner> allEnabled();
}
