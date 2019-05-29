package com.yhh.hbao.core.validator;




import com.yhh.hbao.core.utils.NumberValidationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by yangjj.
 *
 * @DATE 2017/9/8 - 10:43
 * @company WeiMob
 * @description 数字校验处理类
 */
public class NumberValidate implements ConstraintValidator<Number, String> {
    @Override
    public void initialize(Number constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && NumberValidationUtils.isRealNumber(value);
    }

}
