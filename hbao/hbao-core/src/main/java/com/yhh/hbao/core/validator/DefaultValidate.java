package com.yhh.hbao.core.validator;




import com.yhh.hbao.core.utils.Utils;

import java.util.regex.Pattern;

/**
 * @author junjieyang
 * @date 2017/4/25:下午11:03
 * @desc 默认校验类设置
 */
public abstract class DefaultValidate {

    public static final Pattern FORMAT_NUMBER = Pattern.compile("^[0-9]*$");

    public static final Pattern FORMAT_MOBILE = Pattern.compile("^1[34578]\\d{9}$");

    public static final Pattern FORMAT_PASSWORD = Pattern.compile("^[A-Za-z0-9]\\w{5,19}$");

    // 小数点后两位的正实数
    public static final Pattern FORMAT_TWO_DECIMAL = Pattern.compile("^[0-9]+(.[0-9]{1,2})?$");

    /**
     * 默认校验非空
     *
     * @param paramsArgs 参数集合
     */
    public ValidateResponse execute(Object[] paramsArgs) {
        return checkIsBlank(paramsArgs);
    }

    private ValidateResponse checkIsBlank(Object[] paramsArgs) {
        if (Utils.isBlank(paramsArgs)) return ValidateResponse.buildSucc();
        for (Object target : paramsArgs) {
            if (Utils.isNull(target)) {
                return ValidateResponse.build("校验参数不能为空！");
            }
        }
        return ValidateResponse.buildSucc();
    }

    /**
     * 校验是否为数字字符串
     *
     * @param str
     * @return
     */
    protected static boolean isValidNumber(String str) {
        if (str.isEmpty()) {
            return false;
        }

        return FORMAT_NUMBER.matcher(str).matches();
    }

    /**
     * 校验手机号格式是否正确
     *
     * @param str
     * @return
     */
    protected static boolean isValidMobile(String str) {
        if (str.isEmpty()) {
            return false;
        }

        return FORMAT_MOBILE.matcher(str).matches();
    }

    /**
     * 校验密码格式是否正确
     *
     * @param str
     * @return
     */
    protected static boolean isValidPassword(String str) {
        if (str.isEmpty()) {
            return false;
        }

        return FORMAT_PASSWORD.matcher(str).matches();
    }

    /**
     * 校验是否为带两位小数的正实数
     *
     * @param str
     * @return
     */
    protected static boolean isValidTwoDecimal(String str) {
        if (str.isEmpty()) {
            return false;
        }

        return FORMAT_TWO_DECIMAL.matcher(str).matches();
    }

}
