package com.yhh.hbao.core.exception;


import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;

/**
 * 卡券服务业务异常
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:18
 **/
public class CouponBizException extends BaseException {
    public CouponBizException(Integer errorCode, String errorMsg, Object bizData) {
        super(errorCode, errorMsg, bizData);
    }
    public CouponBizException(CouponBizExceptionEnum exception_enum, Object bizData) {
        super(exception_enum.getErrorCode(), exception_enum.getErrorMessage(), bizData);
    }

    public CouponBizException(CouponBizExceptionEnum exception_enum) {
        super(exception_enum.getErrorCode(), exception_enum.getErrorMessage());
    }
}