package com.yhh.hbao.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by yangjj.
 *
 * @DATE 2017/9/8 - 10:42
 * @company WeiMob
 * @description 是否是数字的校验
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberValidate.class)
@Documented
public @interface Number {
    String message() default "输入的参数必须是数字!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
