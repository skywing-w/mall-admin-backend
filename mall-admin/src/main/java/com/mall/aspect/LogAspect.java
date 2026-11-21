package com.mall.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mall.aspect.anno.LogOperation;
import com.mall.entity.OperateLog;
import com.mall.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志切面：拦截标注了 {@code @LogOperation} 的方法，记录操作人、参数、耗时与结果。
 * <p>
 * 切面本身只负责「在请求线程内采集信息并组装实体」，真正的落库交给
 * {@link OperateLogRecorder} 异步执行——因为异步线程拿不到请求上下文（IP 等），
 * 必须在请求线程先把数据抓下来。
 *
 * @author wwty
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    /** 参数与结果落库前的最大长度，避免超长 JSON 撑爆字段 */
    private static final int MAX_TEXT_LENGTH = 4000;

    private final OperateLogRecorder operateLogRecorder;

    @Around("@annotation(com.mall.aspect.anno.LogOperation)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Throwable error = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable t) {
            error = t;
            throw t;
        } finally {
            long cost = System.currentTimeMillis() - start;
            try {
                // 组装放在请求线程内完成（IP、参数都在这里才能拿到）
                operateLogRecorder.record(buildLog(pjp, result, error, cost));
            } catch (Exception e) {
                // 日志采集失败绝不影响业务结果
                log.warn("操作日志采集失败：{}", e.getMessage());
            }
        }
    }

    /**
     * 组装日志实体。
     *
     * @param pjp    切点
     * @param result 方法返回值，异常时为 null
     * @param error  方法抛出的异常，正常返回时为 null
     * @param cost   耗时（毫秒）
     * @return 待落库的日志实体
     */
    private OperateLog buildLog(ProceedingJoinPoint pjp, Object result, Throwable error, long cost) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        LogOperation anno = signature.getMethod().getAnnotation(LogOperation.class);

        OperateLog entity = new OperateLog();
        entity.setUserId(UserHolder.getUserId());
        entity.setUsername(UserHolder.getUsername());
        entity.setModule(anno != null ? anno.module() : "");
        entity.setType(anno != null ? anno.type() : "");
        // 取声明方法的类名，避免拿到 CGLIB 代理类名（xxx$$EnhancerBySpringCGLIB）
        entity.setClassName(signature.getDeclaringTypeName());
        entity.setMethod(signature.getName());
        entity.setCostTime(cost);
        entity.setCreateTime(LocalDateTime.now());

        if (error != null) {
            entity.setResult("异常：" + error.getMessage());
        } else if (result != null) {
            entity.setResult(StrUtil.maxLength(JSONUtil.toJsonStr(result), MAX_TEXT_LENGTH));
        }

        try {
            entity.setParams(StrUtil.maxLength(JSONUtil.toJsonStr(pjp.getArgs()), MAX_TEXT_LENGTH));
        } catch (Exception e) {
            // 参数中存在无法序列化的对象（如文件流）时忽略参数，不影响日志主体
            log.debug("操作日志参数序列化失败：{}", e.getMessage());
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            entity.setIp(getIp(attrs.getRequest()));
        }
        return entity;
    }

    /**
     * 获取真实客户端 IP：优先取代理头，逐级回退。
     *
     * @param request 当前请求
     * @return 客户端 IP
     */
    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多级代理时取第一个地址
            int comma = ip.indexOf(',');
            return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }
}