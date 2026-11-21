package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.UserDto;
import com.mall.entity.User;

public interface UserService extends IService<User> {
    /**
     * 分页查询操作员。
     */
    PageResult<User> page(int page, int pageSize);


    //查询操作员
    User getUserById(Long id);

    //新增操作员
    void addUser(UserDto userDto);

    //修改操作员
    void updateUser(UserDto userDto);


}
