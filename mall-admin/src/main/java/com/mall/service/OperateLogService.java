package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.PageResult;
import com.mall.dto.LogQuery;
import com.mall.entity.OperateLog;

public interface OperateLogService extends IService<OperateLog> {
    PageResult<OperateLog> page(LogQuery query);


}
