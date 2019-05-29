package com.yhh.hbao.core.validator;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yangjj.
 *
 * @DATE 2016/11/8 - 17:57
 * @company WeiMob
 * @description 校验响应对象
 */
@Data
public class ValidateResponse implements Serializable {
    private static final Integer succCode = 0;
    private static final Integer failCode = 1;

    private Integer code;

    private String message;


    public boolean isSuccess() {
        return code != null && code.equals(succCode);
    }

    public boolean isFailure() {
        return code != null && !code.equals(succCode);
    }

    /**
     * 构建真实校验返回对象
     *
     * @param message 模板服务异常类
     * @return 校验对象
     */
    public static ValidateResponse build(String message) {
        ValidateResponse validateResponse = new ValidateResponse();
        validateResponse.setCode(failCode);
        validateResponse.setMessage(message);
        return validateResponse;
    }


    public static ValidateResponse buildSucc() {
        ValidateResponse validateResponse = new ValidateResponse();
        validateResponse.setCode(succCode);
        return validateResponse;
    }
}
