package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private LocalDateTime payTime;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<OrderItem> items;
    @TableField(exist = false)
    private CUser user;
    @TableField(exist = false)
    private Address address;
    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String phone;
}
