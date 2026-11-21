package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Address;

import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {
    List<Address> selectByUserId(Long userId);
}
