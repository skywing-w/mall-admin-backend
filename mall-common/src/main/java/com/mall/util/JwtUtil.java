package com.mall.util;

import cn.hutool.core.util.StrUtil;
import com.mall.properties.MallProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具：HS256 对称签名。
 * <p>
 * 密钥与有效期来自 {@link MallProperties}（配置项 {@code mall.jwt-secret}、{@code mall.jwt-expire-hours}），
 * 不再硬编码在代码里，便于不同环境使用不同密钥。
 * <p>
 * 前端约定：令牌放在请求头 {@code token} 中。
 *
 * @author wwty
 */
@Slf4j
@Component
public class JwtUtil {

    /** HS256 要求密钥不少于 256 bit（32 字节） */
    private static final int MIN_SECRET_LENGTH = 32;

    /** 载荷中存放用户 id 的键名 */
    private static final String CLAIM_ID = "id";

    /** 载荷中存放用户名的键名 */
    private static final String CLAIM_USERNAME = "username";

    private final SecretKey key;

    private final long expireMillis;

    /**
     * 由配置构造签名密钥与有效期。
     *
     * @param properties 项目配置
     * @throws IllegalStateException 密钥未配置或长度不足 32 字节
     */
    public JwtUtil(MallProperties properties) {
        String secret = properties.getJwtSecret();
        if (StrUtil.isBlank(secret) || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_LENGTH) {
            // 配置缺失或过短时直接失败，避免用弱密钥签发出可被伪造的令牌
            throw new IllegalStateException("mall.jwt-secret 未配置或长度不足 32 字节");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = Math.max(1, properties.getJwtExpireHours()) * 60 * 60 * 1000L;
    }

    /**
     * 生成令牌。
     *
     * @param userId   用户 id
     * @param username 用户名
     * @return 已签名的 JWT 字符串
     */
    public String create(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_ID, userId);
        claims.put(CLAIM_USERNAME, username);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析令牌。
     *
     * @param token 令牌原文
     * @return 载荷；令牌为空、签名不合法或已过期时返回 null
     */
    public Claims parse(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 过期、签名错误、格式错误都会抛异常，统一视为无效令牌并记录 debug 日志
            log.debug("JWT 解析失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中取出用户 id。
     *
     * @param token 令牌原文
     * @return 用户 id；令牌无效时返回 null
     */
    public Long getUserId(String token) {
        Claims claims = parse(token);
        if (claims == null) {
            return null;
        }
        Object id = claims.get(CLAIM_ID);
        if (id instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    /**
     * 从令牌中取出用户名。
     *
     * @param token 令牌原文
     * @return 用户名；令牌无效时返回 null
     */
    public String getUsername(String token) {
        Claims claims = parse(token);
        return claims == null ? null : (String) claims.get(CLAIM_USERNAME);
    }
}