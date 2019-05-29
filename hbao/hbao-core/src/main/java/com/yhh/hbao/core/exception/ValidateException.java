package com.yhh.hbao.core.exception;

/**
 * Created by yangjj.
 *
 * @DATE 2018/1/5 - 16:24
 * @company WeiMob
 * @description 校验类异常
 */
public class ValidateException extends BaseException {
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(Integer code, String message) {
        super(code, message);
    }
}
