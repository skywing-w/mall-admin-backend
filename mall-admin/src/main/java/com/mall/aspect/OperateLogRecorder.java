package com.mall.aspect;

import com.mall.config.AsyncConfig;
import com.mall.entity.OperateLog;
import com.mall.mapper.OperateLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 操作日志落库器。
 * <p>
 * 单独抽成 Bean 的原因：{@code @Async} 依赖 Spring 代理生效，
 * 在 {@link LogAspect} 内部直接调用自己的方法（self-invocation）不会走代理，
 * 异步注解等于没写。拆成独立 Bean 由外部调用后才能真的异步执行。
 * <p>
 * 同时注意：异步线程里没有请求上下文（{@code RequestContextHolder} 为空），
 * 因此 IP、请求参数等信息必须由调用方在请求线程内组装好再传进来。
 *
 * @author wwty
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperateLogRecorder {

    private final OperateLogMapper operateLogMapper;

    /**
     * 异步写入一条操作日志。
     *
     * @param operateLog 已组装完整的日志实体
     */
    @Async(AsyncConfig.LOG_EXECUTOR)
    public void record(OperateLog operateLog) {
        try {
            operateLogMapper.insert(operateLog);
        } catch (Exception e) {
            // 日志写入失败不能影响业务，仅记录告警
            log.warn("操作日志写入失败 module={} method={}：{}",
                    operateLog.getModule(), operateLog.getMethod(), e.getMessage());
        }
    }
}