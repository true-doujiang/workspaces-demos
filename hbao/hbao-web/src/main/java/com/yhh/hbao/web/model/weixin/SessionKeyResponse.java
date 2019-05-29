package com.yhh.hbao.web.model.weixin;

import lombok.Data;

/**
 * package: com.weimob.voteplaza.service.model
 * describe: 会话信息响应
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/20/17
 * creat_time: 11:34 AM
 **/
@Data
public class SessionKeyResponse {

    /****
     * openID
     */
    private String openid;
    /****
     *  session KEY
     */
    private String session_key;
    /****
     * 过期时间
     */
    private Integer expires_in;
    /****
     *  UNION ID
     */
    private String unionid;
    /****
     * 错误码
     */
    private Integer errcode;
    /****
     *  错误信息
     */
    private String errmsg;

}
