package com.yhh.hbao.core.validator;


import com.yhh.hbao.core.exception.BaseException;
import com.yhh.hbao.core.utils.ClazzUtils;
import com.yhh.hbao.core.utils.Utils;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by yangjj.
 *
 * @DATE 2017/8/15 - 10:39
 * @company WeiMob
 * @description 校验工具类
 */
public class ValidateUtils {
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            String validateError = "";
            int size = constraintViolations.size();
            int i = 0;
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                if (i == size - 1) {
                    validateError += constraintViolation.getMessage();
                } else {
                    validateError += constraintViolation.getMessage() + ";";
                }
                i++;
            }
            throw new BaseException(validateError);
        }
    }

    public static <T> void validate(T t, JsrValidateEnums validateEnums) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            String validateError = "";
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                Field targetField = ClazzUtils.getDeclaredField(constraintViolation.getRootBeanClass(),
                        constraintViolation.getPropertyPath().toString());
                boolean checkResult = checkJsrValidate(targetField, validateEnums);
                if (checkResult) {
                    validateError += constraintViolation.getMessage() + ";";
                }
            }
            if (!Utils.isBlank(validateError)) {
                validateError = validateError.substring(0, validateError.lastIndexOf(";"));
                throw new BaseException(validateError);
            }

        }
    }

    private static boolean checkJsrValidate(Field targetField, JsrValidateEnums targetEnum) {
        if (targetEnum.equals(JsrValidateEnums.DEFAULT)) return true;
        if (targetField != null) {
            JsrValidate jsrValidate = AnnotationUtils.findAnnotation(targetField, JsrValidate.class);
            if (jsrValidate != null) {
                JsrValidateEnums[] jsrValidateEnums = jsrValidate.method();
                for (JsrValidateEnums temp : jsrValidateEnums) {
                    if (temp.equals(targetEnum)) return true;
                }
                return false;
            }
        }
        return true;
    }
}
