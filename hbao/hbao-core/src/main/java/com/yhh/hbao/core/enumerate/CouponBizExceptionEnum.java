package com.yhh.hbao.core.enumerate;

/**
 * 业务异常枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:19
 **/
public enum CouponBizExceptionEnum {

    CAMPAIGN_IS_NOT_FOUND(100001,"未找到活动"),
    CAMPAIGN_LEVEL_IS_NOT_FOUND(100002,"别贪心"),
    CAMPAIGN_COUNT_IS_ERROR(100002,"活动库存不足"),
    CAMPAIGN_IS_NOT_ARRIVING(100002,"当前无活动在进行中"),
    RECEIVE_CAMPAIGN_IS_ERROR(200001,"领取活动失败"),
    RECEIVELOGS_IS_NOT_FOUND(200002,"未找到领取记录"),
    RECEIVELOGS_IS_NOT_BROKENING(200003,"红包已拆完"),
    BROKEN_RECEIVE_NOT_ERROR(300001,"拆取领取记录失败"),
    BROKEN_USER_IS_BROKEND(300002,"不能重复拆"),
    BROKEN_USER_SELF_BROKEND(300003,"不能拆取自己的红包"),
    USER_COUPON_RECORD_NOT_FOUND(400000,"没有查询到该卡劵"),
    USER_COUPON__EXCHANGE_ERROR(400001,"用户卡券兑换失败"),
    USER_COUPON_RECEIVE_EXCHANGE_ERROR(400002,"卡券状态错误,不允许兑换"),
    USER_RECEIVE_ALL_COUNT_ERROR(500001,"当前用户已超过累计上限"),
    USER_RECEIVE_DAYS_COUNT_ERROR(500002,"当前用户已超过单日累计上限"),
    COUPON_INFO_CODE_EXHAUSTED(600000, "无卡劵可以兑换"),

    AUTH_USER_LOGIN_PARAM_IS_NULL(700000,"登录参数必须包好 {\n" +
            "\t\"code\":\"123\",\n" +
            "\t\"encryptedData\":\"456\",\n" +
            "\t\"iv\":\"789\",\n" +
            "\t\"sessionKeyResponse\":{\n" +
            "\t\t\"openId\":\"openidfjsal\",\n" +
            "\t\t\"unionId\":\"fghjkl5678\",\n" +
            "\t\t\"session_key\":\"797dddgh900889\"\n" +
            "\t},\n" +
            "\t\"wxUserInfo\":{\n" +
            "\t\t\"openId\":\"openidfjsal\",\n" +
            "\t\t\"unionId\":\"fghjkl5678\",\n" +
            "\t\t\"nickName\":\"yhh\",\n" +
            "\t\t\"gender\":\"1\",\n" +
            "\t\t\"city\":\"上海\",\n" +
            "\t\t\"province\":\"上海\",\n" +
            "\t\t\"country\":\"中国\",\n" +
            "\t\t\"avatarUrl\":\"http:weixin.com/ljajd/akjdk\",\n" +
            "\t\t\"language\":\"zh_CN\"\n" +
            "\t}\n" +
            "}"),
    AUTH_USER_LOGIN_TOKEN_IS_NULL(700001,"当前用户未登陆授权,请登陆授权后访问"),
    AUTH_USER_LOGIN_IS_ERROR(700002,"登录错误,未获取到用户的相关信息"),
    REQUEST_IS_TOO_TRIVIAL(700003, "操作太频繁，请稍后再试"),
    DATA_BASE_OPTION_ERROR(888888,"数据库操作异常,请联系管理员"),
    WX_INVOKER_IS_ERROR(900000,"微信服务调用异常!"),
    DATA_PERMISSION_OPTION_ERROR(999998,"您没有当前操作权限,请联系管理员"),
    GLOBAL_FAIL(999999, "系统未知异常"),
    GLOBAL_SUCCESS(0, "成功")
    ;

    /****
     * 异常码
     */
    private Integer errorCode;
    /****
     * 异常信息
     */
    private String errorMessage;


    CouponBizExceptionEnum(Integer errorCode, String errorMessage) {

        this.errorCode=errorCode;
        this.errorMessage=errorMessage;

    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
