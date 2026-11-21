package com.mall.controller;

import com.mall.aspect.anno.LogOperation;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.RoleQuery;
import com.mall.entity.Role;
import com.mall.exception.BusinessException;
import com.mall.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public Result<PageResult<Role>> page(RoleQuery query) {
        return Result.success(roleService.page(query));
    }

    @GetMapping("/all")
    public Result<List<Role>> all() {
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @PostMapping
    @LogOperation(module = "角色", type = "新增")
    public Result<Void> add(@RequestBody Role role) {
        if (role.getName() == null || role.getLabel() == null) {
            throw new BusinessException("角色名称和标识不能为空");
        }
        roleService.save(role);
        return Result.success();
    }

    @PutMapping
    @LogOperation(module = "角色", type = "修改")
    public Result<Void> update(@RequestBody Role role) {
        if (role.getId() == null) throw new BusinessException("id 不能为空");
        roleService.updateById(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @LogOperation(module = "角色", type = "删除")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.success();
    }
}
