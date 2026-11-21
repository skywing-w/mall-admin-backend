package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> listByUserId(Long userId);
}
