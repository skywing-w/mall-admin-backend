package com.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目自定义配置项（配置前缀 {@code mall}）。
 * <p>
 * 对应 {@code application-dev.yml} 中的：
 * <pre>
 * mall:
 *   upload-dir: D:/mall-admin/uploads/
 *   jwt-secret: mall-admin-secret
 *   jwt-expire-hours: 12
 * </pre>
 * 未配置时使用下方默认值，保证本地直接启动不会因缺配置而失败。
 *
 * @author wwty
 */
@Data
@Component
@ConfigurationProperties(prefix = "mall")
public class MallProperties {

    /** 文件上传根目录，末尾不带斜杠也兼容（拼接时会自动补齐） */
    private String uploadDir = "./uploads/";

    /** JWT 签名密钥，长度需不少于 32 字节（HS256 要求） */
    private String jwtSecret = "mall-admin-default-secret-key-please-change-in-production";

    /** JWT 有效期（小时），默认 12 小时 */
    private long jwtExpireHours = 12L;
}