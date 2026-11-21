package com.mall.aspect.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在 Controller 方法上，触发操作日志记录。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogOperation {
    /** 模块名 */
    String module() default "";
    /** 操作类型，如 新增 / 修改 / 删除 */
    String type() default "";
}
