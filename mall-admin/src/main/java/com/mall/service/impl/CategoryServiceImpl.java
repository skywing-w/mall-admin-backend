package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.CategoryQuery;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public PageResult<Category> page(CategoryQuery query) {
        IPage<Category> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getName()), Category::getName, query.getName())
                .eq(query.getParentId() != null, Category::getParentId, query.getParentId())
                .orderByAsc(Category::getSort)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public List<Category> tree() {
        List<Category> all = baseMapper.selectAllEnabled();
        return buildTree(all, 0L);
    }

    private List<Category> buildTree(List<Category> all, Long parentId) {
        Map<Long, List<Category>> map = all.stream()
                .collect(Collectors.groupingBy(Category::getParentId));
        List<Category> roots = new ArrayList<>();
        for (Category c : all) {
            if (parentId.equals(c.getParentId())) {
                c.setChildren(buildTree(all, c.getId()));
                roots.add(c);
            }
        }
        return roots;
    }
}
