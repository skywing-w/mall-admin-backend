package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Address;
import com.mall.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/by-user/{userId}")
    public Result<List<Address>> byUser(@PathVariable Long userId) {
        return Result.success(addressService.listByUserId(userId));
    }
}
