package com.mall.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.mapper.LoginMapper;
import com.mall.service.LoginService;
import com.mall.util.JwtUtil;
import com.mall.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录服务实现。
 * <p>
 * 说明：数据库中的密码为 MD5 摘要，因此这里先把明文转 MD5 再比对。
 * MD5 无盐哈希强度较低，但改动会同时涉及存量数据迁移与新增依赖（如 spring-security-crypto），
 * 故本次保持不变，仅在文档中标注为待改进项。
 *
 * @author wwty
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    private final JwtUtil jwtUtil;

    @Override
    public Result<LoginVo> login(String username, String password) {
        // 库里存的是 MD5 密码，先把前端传来的明文转成 MD5 再比对
        User user = baseMapper.login(username, SecureUtil.md5(password));
        if (user == null) {
            // 统一提示，不区分「用户不存在」与「密码错误」，避免账号枚举
            log.warn("登录失败 username={}", username);
            return Result.error("用户名或密码错误");
        }

        // 只把前端需要的字段放进 LoginVo，密码不外传
        LoginVo vo = new LoginVo();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setRoleLabel(user.getRoleLabel());
        vo.setToken(jwtUtil.create(user.getId(), user.getUsername()));
        log.info("登录成功 username={} id={}", user.getUsername(), user.getId());
        return Result.success(vo);
    }
}