package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("c_user")
public class CUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private String nickname;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
