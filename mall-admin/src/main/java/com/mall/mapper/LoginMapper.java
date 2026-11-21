package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface LoginMapper extends BaseMapper<User> {
    /**
     * 按用户名和密码查询用户（密码需先转为 MD5）。
     */
    User login(@Param("username") String username, @Param("password") String password);
}
