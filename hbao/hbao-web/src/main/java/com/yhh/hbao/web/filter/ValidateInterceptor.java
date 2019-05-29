package com.yhh.hbao.web.filter;


import com.yhh.hbao.core.utils.FastJsonUtils;
import com.yhh.hbao.core.utils.LogUtils;
import com.yhh.hbao.core.utils.Utils;
import com.yhh.hbao.core.validator.JsrValidateEnums;
import com.yhh.hbao.core.validator.Validate;
import com.yhh.hbao.core.validator.ValidateUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by yangjj.
 *
 * @DATE 2017/8/8 - 2:08
 * @company WeiMob
 * @description 校验拦截器
 * <p>支持jsr 303的校验</p>
 */
@Component("validateInterceptor")
public class ValidateInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        //如果参数为空 则不进行校验
        if (Utils.isBlank(args)) {
            return invocation.proceed();
        }
        Method method = invocation.getMethod();
        Validate validate = AnnotationUtils.findAnnotation(method, Validate.class);
        //是否忽略校验
        if (validate != null) {
            //仅仅校验第一个参数
            Object arguments = args[0];
            LogUtils.info(getClass(), "需要校验的数据内容是:" + FastJsonUtils.beanToJson(arguments));
            JsrValidateEnums jsrValidateEnums = validate.jsrValidate();
            if (!jsrValidateEnums.equals(JsrValidateEnums.DEFAULT)) {
                ValidateUtils.validate(arguments, jsrValidateEnums);
            } else {
                ValidateUtils.validate(arguments);
            }
        }
        return invocation.proceed();
    }
}
