package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.DeptQuery;
import com.mall.entity.Dept;

import java.util.List;

public interface DeptService extends IService<Dept> {
    PageResult<Dept> page(DeptQuery query);
    /** 获取全部启用的部门（树形） */
    List<Dept> tree();
}
