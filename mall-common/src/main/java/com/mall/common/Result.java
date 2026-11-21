package com.mall.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果。
 * code: 1=成功, 0=失败
 */
@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 1;
        r.msg = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        return error(0, msg);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.code = code;
        r.msg = msg;
        return r;
    }
}
