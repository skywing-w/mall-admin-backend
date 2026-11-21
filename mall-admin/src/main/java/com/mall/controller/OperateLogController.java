package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.LogQuery;
import com.mall.entity.OperateLog;
import com.mall.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operate-logs")
@RequiredArgsConstructor
public class OperateLogController {

    private final OperateLogService operateLogService;

    @GetMapping
    public Result<PageResult<OperateLog>> page(LogQuery query) {
        return Result.success(operateLogService.page(query));
    }
}
