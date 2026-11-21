package com.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.LogQuery;
import com.mall.entity.OperateLog;
import com.mall.mapper.OperateLogMapper;
import com.mall.service.OperateLogService;
import org.springframework.stereotype.Service;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    @Override
    public PageResult<OperateLog> page(LogQuery query) {
        IPage<OperateLog> page = new Page<>(query.getPage(), query.getPageSize());
        lambdaQuery()
                .like(StrUtil.isNotBlank(query.getUsername()), OperateLog::getUsername, query.getUsername())
                .like(StrUtil.isNotBlank(query.getModule()), OperateLog::getModule, query.getModule())
                .orderByDesc(OperateLog::getId)
                .page(page);
        return PageResult.of(page.getTotal(), page.getRecords());
    }


}
