package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.vo.LoginVo;

public interface LoginService extends IService<User> {
    /**
     * 登录校验，成功返回 LoginVo，失败返回错误提示。
     */
    Result<LoginVo> login(String username, String password);
}
