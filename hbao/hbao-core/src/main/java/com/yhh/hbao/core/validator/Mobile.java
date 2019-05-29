package com.yhh.hbao.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by yangjj.
 *
 * @DATE 2017/7/20 - 18:59
 * @company WeiMob
 * @description 校验是否是手机号码
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidate.class)
@Documented
public @interface Mobile {
    String message() default "手机号码格式有问题!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
