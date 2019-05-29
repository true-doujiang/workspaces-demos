package com.yhh.hbao.core.validator;

import java.lang.annotation.*;

/**
 * Created by yangjj.
 *
 * @DATE 2016/4/14 - 10:13
 * @company WeiMob
 * @description 自定义注解校验测试类型
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {
    /**
     * 校验类型 默认非空
     *
     * @return 校验类型枚举
     */
    ValidateType name() default ValidateType.NOT_NULL;

    /**
     * 校验处理类的名称
     *
     * @return 处理类名称
     */
    Class<?> validateClass() default DefaultValidate.class;

    /**
     * 默认容器类相关的校验数据
     *
     * @return 容器类处理的名称
     */
    String containClassName() default "";

    /**
     * 如果jsr 校验 目标规则
     *
     * @return 设置规则
     */
    JsrValidateEnums jsrValidate() default JsrValidateEnums.DEFAULT;
}
