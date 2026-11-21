package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.DeptQuery;
import com.mall.entity.Dept;
import com.mall.mapper.DeptMapper;
import com.mall.service.DeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public PageResult<Dept> page(DeptQuery query) {
        IPage<Dept> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getName()), Dept::getName, query.getName())
                .orderByAsc(Dept::getSort)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public List<Dept> tree() {
        List<Dept> all = baseMapper.selectAllEnabled();
        return buildTree(all, 0L);
    }

    private List<Dept> buildTree(List<Dept> all, Long parentId) {
        Map<Long, List<Dept>> map = all.stream()
                .collect(Collectors.groupingBy(Dept::getParentId));
        List<Dept> roots = new ArrayList<>();
        for (Dept d : all) {
            if (parentId.equals(d.getParentId())) {
                d.setChildren(buildTree(all, d.getId()));
                roots.add(d);
            }
        }
        return roots;
    }
}
