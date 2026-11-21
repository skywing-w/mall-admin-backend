package com.mall.util;

/**
 * 当前登录用户上下文（基于 ThreadLocal）。
 * 由 LoginInterceptor 写入，由业务代码读取。
 */
public final class UserHolder {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    private UserHolder() {}

    public static void set(Long userId, String username) {
        USER_ID.set(userId);
        USERNAME.set(username);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }
}
