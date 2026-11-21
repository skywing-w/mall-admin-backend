package com.mall.exception;

import lombok.Getter;

/**
 * 业务异常，用于受控的业务错误。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        this(0, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
