package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.DeptQuery;
import com.mall.entity.Dept;
import com.mall.exception.BusinessException;
import com.mall.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @GetMapping
    public Result<PageResult<Dept>> page(DeptQuery query) {
        return Result.success(deptService.page(query));
    }

    @GetMapping("/all")
    public Result<List<Dept>> all() {
        return Result.success(deptService.list());
    }

    @GetMapping("/tree")
    public Result<List<Dept>> tree() {
        return Result.success(deptService.tree());
    }

    @GetMapping("/{id}")
    public Result<Dept> getById(@PathVariable Long id) {
        return Result.success(deptService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "部门", type = "新增")
    public Result<Void> add(@RequestBody Dept dept) {
        if (dept.getName() == null) throw new BusinessException("部门名称不能为空");
        if (dept.getParentId() == null) dept.setParentId(0L);
        deptService.save(dept);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "部门", type = "修改")
    public Result<Void> update(@RequestBody Dept dept) {
        if (dept.getId() == null) throw new BusinessException("id 不能为空");
        deptService.updateById(dept);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "部门", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.removeById(id);
        return Result.success();
    }
}
