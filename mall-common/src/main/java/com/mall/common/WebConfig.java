package com.mall.common;

import com.mall.properties.MallProperties;
import com.mall.util.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web 层配置：登录拦截器 + 上传文件的静态资源映射。
 *
 * @author wwty
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /** 免登录路径：登录接口本身、错误页、接口文档、静态上传资源 */
    private static final String[] EXCLUDE_PATHS = {
            "/login",
            "/error",
            "/doc.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/uploads/**"
    };

    private final JwtInterceptor jwtInterceptor;

    private final MallProperties properties;

    /**
     * 跨域配置。
     * <p>
     * 浏览器跨域预检（OPTIONS）若没有对应配置，会被 Spring 判定为
     * 「Invalid CORS request」并返回 403。这里放开开发期来源；
     * 上线时请把 allowedOriginPatterns 收窄为实际域名。
     * 鉴权走自定义请求头 token 而非 Cookie，因此不需要开启 allowCredentials。
     *
     * @param registry 跨域注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("token")
                .maxAge(3600);
        log.info("CORS 已开启：允许所有来源的跨域请求（生产环境请收窄）");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    /**
     * 把上传目录映射为可访问的静态资源路径。
     * <p>
     * 注意 context-path 为 {@code /api}，因此完整访问地址是 {@code /api/uploads/...}。
     *
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = properties.getUploadDir();
        if (!uploadDir.endsWith("/") && !uploadDir.endsWith("\\")) {
            uploadDir = uploadDir + "/";
        }
        File dir = new File(uploadDir).getAbsoluteFile();
        if (!dir.exists() && !dir.mkdirs()) {
            // 目录无法创建时只告警：不影响其它接口启动，上传时会再次报错并给出原因
            log.warn("上传目录不存在且创建失败：{}", dir.getAbsolutePath());
        }
        // 用绝对路径构造 file: 资源，相对路径在部分容器下会解析到临时目录导致 404
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(dir.toURI().toString());
        log.info("上传目录映射完成：/uploads/** -> {}", dir.getAbsolutePath());
    }
}