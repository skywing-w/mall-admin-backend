package com.mall.vo;

import lombok.Data;

/**
 * 登录成功后返回给前端的信息。
 */
@Data
public class LoginVo {
    private Long id;
    private String name;
    private String username;
    private String avatar;
    private String roleLabel;
    private String token;
    private String permission = "*";
}
