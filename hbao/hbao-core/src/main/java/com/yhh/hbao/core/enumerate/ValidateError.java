package com.yhh.hbao.core.enumerate;

import lombok.Getter;

/**
 * Created by yangjj.
 *
 * @DATE 2018/1/5 - 16:27
 * @company WeiMob
 * @description 校验类相关错误 从 900001 ~ 900999
 */
public enum ValidateError {

    REQ_PARAM_BLANK_ERROR(900001, "该字段不能为空"),
    CONTAIN_NOT_FOUND_ERROR(900002, "获取校验类未空"),
    UN_KNOW_ERROR(900999, "校验类未知错误");

    @Getter
    private Integer errorCode;

    @Getter
    private String errorMsg;


    ValidateError(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
