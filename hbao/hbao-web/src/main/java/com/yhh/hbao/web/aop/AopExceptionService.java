package com.yhh.hbao.web.aop;

import com.yhh.hbao.core.enumerate.ValidateError;
import com.yhh.hbao.core.exception.BaseException;
import com.yhh.hbao.core.exception.ValidateException;
import com.yhh.hbao.core.utils.LogUtils;
import com.yhh.hbao.core.utils.Utils;
import com.yhh.hbao.core.validator.DefaultValidate;
import com.yhh.hbao.core.validator.Validate;
import com.yhh.hbao.core.validator.ValidateResponse;
import com.yhh.hbao.core.validator.ValidateType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 异常捕获AOP
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-12 下午1:43
 **/
@Aspect
@Component
public class AopExceptionService {

    /**
     * 默认执行的校验方法
     */
    private static final String DEFAULT_EXECUTE_METHOD = "execute";

    @Autowired
    private ApplicationContext applicationContext;
    @Around("execution(* com.yhh.hbao.service.*.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        validate(joinPoint.getArgs(), joinPoint);
        Object obj = joinPoint.proceed();
        return obj;

    }
    /**
     * 校验参数
     *
     * @param paramsArgs 方法入参
     * @param pjp        代理对象
     */
    private void validate(Object[] paramsArgs, ProceedingJoinPoint pjp) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method targetMethod = getMethod(pjp);
        Validate validate = AnnotationUtils.findAnnotation(targetMethod, Validate.class);
        if (validate != null) {
            ValidateType validateType = validate.name();
            switch (validateType) {
                case NOT_NULL:
                    validateIsNull(paramsArgs);
                    break;
                case NOT_EMPTY:
                    validateIsBlank(paramsArgs);
                    break;
                case CUSTOM:
                    Class<?> validateClass = validate.validateClass();
                    checkIsCustom(paramsArgs, validateClass, targetMethod);
                    break;
                case CONTAIN:
                    String containClassName = validate.containClassName();
                    checkIsContain(containClassName, targetMethod, paramsArgs);
                    break;
                default:
                    validateIsNull(paramsArgs);
                    break;
            }
        }
    }
    private void checkIsContain(String containClassName, Method targetMethod, Object[] paramsArgs) {
        try {

            if (Utils.isBlank(containClassName)) {
                throw new ValidateException(ValidateError.CONTAIN_NOT_FOUND_ERROR.getErrorCode(), ValidateError.CONTAIN_NOT_FOUND_ERROR.getErrorMsg());
            }
            Object validateBean = applicationContext.getBean(containClassName);
            if (validateBean == null) {
                throw new ValidateException(ValidateError.CONTAIN_NOT_FOUND_ERROR.getErrorCode(), ValidateError.CONTAIN_NOT_FOUND_ERROR.getErrorMsg());
            }
            Method method = validateBean.getClass().getMethod(targetMethod.getName(), targetMethod.getParameterTypes());
            ValidateResponse validateResponse = (ValidateResponse) method.invoke(validateBean, paramsArgs);
            if (!validateResponse.isSuccess()) {
                throw new BaseException(validateResponse.getCode(), validateResponse.getMessage());
            }
        } catch (BaseException ex) {
            throw ex;
        } catch (Exception e) {
            LogUtils.error(getClass(), "调用校验类出现系统未知异常", e);
            throw new ValidateException(ValidateError.UN_KNOW_ERROR.getErrorCode(), ValidateError.UN_KNOW_ERROR.getErrorMsg());
        }
    }

    private void checkIsCustom(Object[] paramsArgs, Class<?> validateClazz, Method validateMethod) {
        try {
            Method method = validateClazz.getName().equals(DefaultValidate.class.getName()) ? validateClazz.getMethod(DEFAULT_EXECUTE_METHOD) : validateClazz.getMethod(validateMethod.getName(), validateMethod.getParameterTypes());
            Object targetObject = validateClazz.newInstance();
            ValidateResponse result = (ValidateResponse) method.invoke(targetObject, paramsArgs);
            if (!result.isSuccess()) {
                throw new BaseException(result.getCode(), result.getMessage());
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            LogUtils.error(getClass(), "调用校验类出现系统未知异常", e);
            throw new ValidateException(ValidateError.UN_KNOW_ERROR.getErrorCode(), ValidateError.UN_KNOW_ERROR.getErrorMsg());
        }
    }

    private void validateIsNull(Object[] paramsArgs) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (Utils.isBlank(paramsArgs)) {
            return;
        }
        for (Object target : paramsArgs) {
            Field[] fields = target.getClass().getDeclaredFields();
            for(Field field:fields){
                NotNull notNull = AnnotationUtils.findAnnotation(field,NotNull.class);
                if (null!=notNull&&Utils.isNull(target.getClass().getMethod("get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1)).invoke(target))) {
                    throw new ValidateException(ValidateError.REQ_PARAM_BLANK_ERROR.getErrorCode(), "["+field.getName()+"]"+ValidateError.REQ_PARAM_BLANK_ERROR.getErrorMsg());
                }
            }
            if (Utils.isNull(target)) {
                throw new ValidateException(ValidateError.REQ_PARAM_BLANK_ERROR.getErrorCode(), ValidateError.REQ_PARAM_BLANK_ERROR.getErrorMsg());
            }
        }
    }


    private void validateIsBlank(Object[] paramsArgs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Utils.isBlank(paramsArgs)) {
            return;
        }
        for (Object target : paramsArgs) {
            Field[] fields = target.getClass().getDeclaredFields();
            if (Utils.isBlank(target)) {
                for(Field field:fields){
                    NotNull notNull = AnnotationUtils.findAnnotation(field,NotNull.class);
                    if (null!=notNull&&Utils.isNull(target.getClass().getMethod("get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1)).invoke(target))) {
                        throw new ValidateException(ValidateError.REQ_PARAM_BLANK_ERROR.getErrorCode(), "["+field.getName()+"]"+ValidateError.REQ_PARAM_BLANK_ERROR.getErrorMsg());
                    }
                }
                if (Utils.isNull(target)) {
                    throw new ValidateException(ValidateError.REQ_PARAM_BLANK_ERROR.getErrorCode(), ValidateError.REQ_PARAM_BLANK_ERROR.getErrorMsg());
                }
            }
        }
    }


    /**
     * 获取代理的方法
     *
     * @param pjp 代理对象
     * @return 代理的真实方法
     */
    private Method getMethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        return methodSignature.getMethod();
    }

}