package com.mall.controller;

import com.mall.common.Result;
import com.mall.dto.LoginDTO;
import com.mall.service.LoginService;
import com.mall.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * 登录。
     */
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDTO loginDTO) {
        return loginService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }
}
