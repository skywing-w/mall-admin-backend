package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Banner;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BannerMapper extends BaseMapper<Banner> {
    @Select("SELECT * FROM banner WHERE status = 1 ORDER BY sort ASC")
    List<Banner> selectAllEnabled();
}
