package com.yhh.hbao.web.utils;

import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.constants.Constants;
import com.yhh.hbao.core.utils.MD5Encrypt;
import com.yhh.hbao.web.model.weixin.TokenInfo;

import java.util.Date;
import java.util.UUID;

/**
 * package: com.weimob.voteplaza
 * describe: Token MD5
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/20/17
 * creat_time: 3:59 PM
 **/

public class TokenUtils {
    private static  final String tokenFormat="%s_%s_%s_%s";

    /****
     * token 信息
     */
    private static ThreadLocal<TokenInfo> tokenThreadLocal = new ThreadLocal<>();
    /****
     *  根据用户信息生成Token
     * @param userInfoDto
     * @return
     */
    public static String  buildToken(UserInfoDto userInfoDto) {
        if(null!=userInfoDto&&null!=userInfoDto.getId()){
            String openId = userInfoDto.getOpenId();
            Long userId = userInfoDto.getId();
            String value = String.format(tokenFormat, Constants.TOKEN_DD_XCX_MARKETING_COUPON_SIGN_KEY,openId,userId,new Date(), UUID.randomUUID());
            return MD5Encrypt.md5(value);
        }
        return null;
    }

    /*****
     * 设置TokenInfo
     * @param token
     */
    public static void  putToken(TokenInfo token) {
        tokenThreadLocal.set(token);
    }

    /*****
     * 获取token 信息
     * @return
     */
    public static TokenInfo getToken() {
        return tokenThreadLocal.get();
    }




}
