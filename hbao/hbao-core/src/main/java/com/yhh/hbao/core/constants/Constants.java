package com.yhh.hbao.core.constants;

/**
 * package: com.weimob.voteplaza.constant
 * describe: 常量池
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/12/17
 * creat_time: 6:32 PM
 **/

public final class Constants {

    /** http相关参数 **/
    public static final String DEFAULT_CHARSET 							= "UTF-8";
    public static final String DEFAULT_CONTENT_TYPE_NAME				= "content-type";
    public static final String DEFAULT_CONTENT_TYPE_VALUE 				= "application/json;charset=UTF-8";

    /** http请求默认读取超时 */
    public static final Integer DEFAULT_HTTP_REQUEST_READ_TIMEOUT 		= 3000;
    /** http请求默认连接超时超时 */
    public static final Integer DEFAULT_HTTP_REQUEST_CONNECT_TIMEOT     = 3000;
    /**
     * 7天缓存
     */
    public static final Integer DEFAULT_REIDS_EXPIRE_TIME 				= 7*24*60*60;

    /**
     * 微信用户缓存一天
     */
    public static final Integer DEFAULT_WX_AUTH_REIDS_EXPIRE_TIME 		= 1*24*60*60;
    /**
     * 5分钟
     */
    public static final Integer REIDS_AOP_EXPIRE_TIME 				    = 5*60;
    public static final String REDIS＿NXXX 								= "NX";
    public static final String REDIS＿EXPX 								= "EX";

    /****
     * 微信应用Access_token
     */
    public static final String WEIXIN_APPLICATION_ACCESS_TOEKN_KEY       = "APPLICATION_ACCESS_TOEKN_KEY";

    /****
     * 默认头像地址
     */
    public static final String HEAD_DEFAULT_URL                          ="https://www.baidu.com/img/baidu_jgylogo3.gif";

    public static final String SOA_RESPONSE_SUCCESS_CODE 				 = "000000";


    /**
     * 店达-等会-核销小程序-安全-KEY
     * Token加密KEY
     */
    public static final String TOKEN_DD_XCX_MARKETING_COUPON_SIGN_KEY   = "TOKEN_DD_XCX_MARKETING_COUPON_SIGN_KEY";

    /**
     * 应用中使用到的常量
     */
    public static final Integer USER_COUPON_EXCHANGE_LIMIT                  =  7;
    public static final Integer USER_COUPON_CASH_LIMIT                      =  1;

    /**
     * 计算距离时使用的经纬度
     */
    // TODO 临时解决方案
    public static final Double[] DISTANCE_LEVEL  = new Double[] {
        0.0898311/2,
        0.0898311*1,
        0.0898311*1.5,
        0.0898311*2,
        0.0898311*2.5
    };

    /**
     * 是否已核销 0:否 1:是'
     */
    public static final Integer COUPON_IS_VERIFICATION_NO       =  0;
    public static final Integer COUPON_IS_VERIFICATION_YES      =  1;

    /**
     * 来源:1:自主生成 2:第三方生成',
     */
    public static final Integer COUPON_SOURCE_MYSELF            =  1;
    public static final Integer COUPON_SOURCE_OTHER             =  2;

}
