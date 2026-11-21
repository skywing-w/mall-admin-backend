package com.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Address;
import com.mall.mapper.AddressMapper;
import com.mall.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
