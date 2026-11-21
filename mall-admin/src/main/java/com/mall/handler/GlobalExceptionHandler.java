package com.mall.handler;

import com.mall.common.Result;
import com.mall.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理。
 * <p>
 * 把各类异常统一转换为 {@link Result} 结构，保证前端只需判断 {@code code}；
 * 同时避免把数据库错误等内部细节直接暴露给调用方。
 *
 * @author wwty
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 兜底异常对外的统一提示，具体原因只写日志 */
    private static final String INTERNAL_ERROR_MSG = "系统繁忙，请稍后重试";

    /**
     * 业务异常：属于可预期的受控错误，把原始提示返回给前端。
     *
     * @param e   业务异常
     * @param req 当前请求，用于日志定位
     * @return 错误响应，code 取自异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e, HttpServletRequest req) {
        log.warn("业务异常 {} - {}", req.getRequestURI(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 唯一键冲突：多为重复提交，提示用户即可。
     *
     * @param e 数据库唯一键冲突异常
     * @return 错误响应
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicate(DuplicateKeyException e) {
        log.warn("唯一键冲突：{}", e.getMessage());
        return Result.error("数据已存在，请勿重复添加");
    }

    /**
     * 请求体参数校验失败（@Valid + @RequestBody）。
     *
     * @param e 校验异常
     * @return 错误响应，携带第一条字段校验信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + "：" + fe.getDefaultMessage())
                .orElse("参数校验失败");
        log.debug("参数校验失败：{}", msg);
        return Result.error(msg);
    }

    /**
     * 表单 / 查询参数绑定失败。
     *
     * @param e 绑定异常
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBind(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + "：" + fe.getDefaultMessage())
                .orElse("参数绑定失败");
        log.debug("参数绑定失败：{}", msg);
        return Result.error(msg);
    }

    /**
     * 缺少必填请求参数。
     *
     * @param e 缺参异常
     * @return 错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParam(MissingServletRequestParameterException e) {
        log.debug("缺少请求参数：{}", e.getParameterName());
        return Result.error("缺少必要参数：" + e.getParameterName());
    }

    /**
     * 参数类型不匹配，例如路径变量传了非数字。
     *
     * @param e 类型转换异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.debug("参数类型错误：{}", e.getName());
        return Result.error("参数类型错误：" + e.getName());
    }

    /**
     * 兜底异常。
     * <p>
     * 注意：这里不能把 {@code e.getMessage()} 返回给前端——它可能包含 SQL 语句、
     * 表名、文件路径等内部信息，属于信息泄露。详细信息只写服务端日志。
     *
     * @param e   未预期异常
     * @param req 当前请求
     * @return 统一的失败响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleAll(Exception e, HttpServletRequest req) {
        log.error("系统异常 {} {}", req.getMethod(), req.getRequestURI(), e);
        return Result.error(INTERNAL_ERROR_MSG);
    }
}