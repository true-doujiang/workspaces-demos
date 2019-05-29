package com.yhh.hbao.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yhh
 * @date 2017/4/25:下午11:08
 * @desc 基础异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException  {
    protected Integer errorCode;

    protected String errorMsg;

    // 业务数据
    private Object bizData;

    public BaseException(Integer errorCode, String errorMsg, Object bizData) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.bizData = bizData;
    }


    public BaseException(String message) {
        super(message);
        //默认设置系统未知异常
        this.errorCode = 999999;
        this.errorMsg = message;
    }

    public BaseException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
