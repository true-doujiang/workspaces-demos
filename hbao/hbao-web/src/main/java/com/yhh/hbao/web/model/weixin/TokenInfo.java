package com.yhh.hbao.web.model.weixin;

import com.yhh.hbao.api.transfer.UserInfoDto;
import lombok.Data;

import java.io.Serializable;

/**
 * package: com.weimob.voteplaza.model
 * describe: 登录票据
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/14/17
 * creat_time: 1:12 PM
 **/
@Data
public class TokenInfo implements Serializable{

    /****
     * 登录Token
     */
    private String token;

    /****
     * Token时间
     */
    private Integer expirationTime;

    /****
     *  用户基本信息
     */
    private UserInfoDto userInfoDto;

    /****
     * 第三方Session key
     */
    private String sessionKey;
}
