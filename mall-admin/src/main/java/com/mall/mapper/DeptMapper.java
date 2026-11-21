package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Dept;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {
    List<Dept> selectAllEnabled();
}
