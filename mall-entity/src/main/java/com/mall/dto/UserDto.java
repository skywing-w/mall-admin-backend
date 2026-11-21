package com.mall.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String password;
    private String username;
    private String name;
    private String phone;
    private String email;
    private Long roleId;
    private Long deptId;
    private Integer status;
}
