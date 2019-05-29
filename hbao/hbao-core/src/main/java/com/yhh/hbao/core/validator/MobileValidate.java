package com.yhh.hbao.core.validator;



import com.yhh.hbao.core.utils.Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by yangjj.
 *
 * @DATE 2017/7/20 - 19:01
 * @company WeiMob
 * @description 手机号码校验处理类
 */
public class MobileValidate implements ConstraintValidator<Mobile, String> {
    @Override
    public void initialize(Mobile constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Utils.isMobile(value);
    }
}
