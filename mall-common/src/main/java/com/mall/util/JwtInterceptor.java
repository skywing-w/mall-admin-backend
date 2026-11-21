package com.mall.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 登录校验拦截器。
 * <p>
 * 从请求头 {@code token} 解析 JWT，解析成功后把用户信息写入 {@link UserHolder}，
 * 供业务代码与操作日志切面读取；请求结束务必清理，避免线程复用导致用户信息串号。
 *
 * @author wwty
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    /** 令牌所在的请求头名称 */
    private static final String TOKEN_HEADER = "token";

    /** 未登录时返回的 JSON 响应体，与 {@code Result} 结构保持一致，便于前端统一处理 */
    private static final String UNAUTHORIZED_BODY = "{\"code\":0,\"msg\":\"未登录或登录已过期\",\"data\":null}";

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 浏览器跨域预检请求不携带自定义请求头，必须直接放行，
        // 否则 OPTIONS 会被判为未登录返回 401，导致前端所有跨域请求失败
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(TOKEN_HEADER);
        Long userId = jwtUtil.getUserId(token);
        if (userId == null) {
            writeUnauthorized(response, request);
            return false;
        }

        UserHolder.set(userId, jwtUtil.getUsername(token));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 必须清理 ThreadLocal，否则 Tomcat 复用线程时会把上一个请求的用户带到下一个请求
        UserHolder.clear();
    }

    /**
     * 输出统一的未登录响应。
     *
     * @param response 当前响应
     * @param request  当前请求，仅用于日志定位
     */
    private void writeUnauthorized(HttpServletResponse response, HttpServletRequest request) {
        log.debug("未登录访问 {} {}", request.getMethod(), request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(UNAUTHORIZED_BODY);
        } catch (Exception e) {
            // 响应已提交或客户端已断开，无需再处理
            log.debug("写入未登录响应失败：{}", e.getMessage());
        }
    }
}