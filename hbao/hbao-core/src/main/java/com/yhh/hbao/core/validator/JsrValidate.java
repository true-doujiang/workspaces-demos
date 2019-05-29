package com.yhh.hbao.core.validator;

import java.lang.annotation.*;

/**
 * 针对jsr 不同的校验规则设置
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsrValidate {
    /**
     * 校验方式 支持多个版本 校验
     * @return
     */
    JsrValidateEnums[] method() default JsrValidateEnums.DEFAULT;
}
