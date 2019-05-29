package com.yhh.hbao.web.exception;

import com.yhh.hbao.core.exception.BaseException;
import com.yhh.hbao.core.utils.LogUtils;
import com.yhh.hbao.web.model.ResultResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResultResponse handler(HttpServletRequest request, Throwable e) {
        String requestURI = request.getRequestURI();
        //打印url 参数 返回值 请求方式
        Map<String, String[]> map = request.getParameterMap();
        String method = request.getMethod();
        LogUtils.error(getClass(), String.format("【请求异常】uri:【%s】 -----------请求方式:【%s】------------请求参数:【%s】", requestURI,method,JSON.toJSON(map)), e);
        String msgId = UUID.randomUUID().toString();
        //如果是方法校验类异常
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            String errMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
            return ResultResponse.fail(101, errMsg);
        } else if (e instanceof NoHandlerFoundException) {
            return ResultResponse.fail(404, "请求地址不存在!");
            //spring的校验类
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            String errMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
            return ResultResponse.fail(102, errMsg);
            //如果是数据绑定错误
        } else if (e instanceof ServletRequestBindingException) {
            ServletRequestBindingException ex = (ServletRequestBindingException) e;
            return ResultResponse.fail(105, ex.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException ex = (HttpRequestMethodNotSupportedException) e;
            return ResultResponse.fail(405, "请求方法不支持" + ex.getMethod());
            //如果是业务校验异常错误
        } else if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            //如果是系统未知异常
            if (baseException.getErrorCode().equals(0)) {
                ResultResponse.error();
            }
            //业务类型错误
            return ResultResponse.fail(baseException.getErrorCode(), baseException.getMessage(),baseException.getBizData());
        } else if (e instanceof JSONException) {
            JSONException jsonException = (JSONException) e;
            if (jsonException.getCause() != null && jsonException.getCause() instanceof IllegalArgumentException) {
                return ResultResponse.fail(102, "参数格式错误");
            } else {
                return ResultResponse.fail(104, jsonException.getMessage());
            }
        } else if (e instanceof AuthorizationException) {
            return ResultResponse.unauthorized();
        }
        LogUtils.error(getClass(), "消息编号:" + msgId + "出现系统未知异常", e);
        return ResultResponse.error();
    }

}
