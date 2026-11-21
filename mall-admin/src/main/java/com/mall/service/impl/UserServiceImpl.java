package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.UserDto;
import com.mall.entity.User;
import com.mall.exception.BusinessException;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    /**
     * 分页查询操作员。
     */
    @Override
    public PageResult<User> page(int page, int pageSize) {
        IPage<User> result = lambdaQuery()
                .orderByDesc(User::getId)
                .page(new Page<>(page, pageSize));
        // 密码不返回给前端
        result.getRecords().forEach(u -> u.setPassword(null));
        return PageResult.of(result.getTotal(), result.getRecords());
    }

    @Override
    public User getUserById(Long id) {
        User user = getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public void addUser(UserDto dto) {
        if (StrUtil.isBlank(dto.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        long count = lambdaQuery().eq(User::getUsername, dto.getUsername()).count();
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(SecureUtil.md5(StrUtil.isBlank(dto.getPassword()) ? DEFAULT_PASSWORD : dto.getPassword()));
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRoleId(dto.getRoleId());
        user.setDeptId(dto.getDeptId());
        user.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        save(user);
    }

    @Override
    public void updateUser(UserDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("id 不能为空");
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRoleId(dto.getRoleId());
        user.setDeptId(dto.getDeptId());
        user.setStatus(dto.getStatus());
        if (StrUtil.isNotBlank(dto.getPassword())) {
            user.setPassword(SecureUtil.md5(dto.getPassword()));
        }
        updateById(user);
    }
}
