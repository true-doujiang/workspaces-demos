package com.yhh.hbao.web.model.weixin;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户信息
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-12 下午7:06
 **/
@Data
public class WxUserInfo implements Serializable {

    /***
     * 用户昵称
     */
    private String nickName;

    /****
     * 用户头像地址
     */
    private String avatarUrl;
    /****
     * 用户性别
     */
    private String gender;
    /****
     * 所属城市
     */
    private String city;
    /****
     * 所属省份
     */
    private String province;

    /****
     * 所属国家
     */
    private String country;
    /****
     * 语言
     */
    private String language;

    /****
     * 用户唯一ID
     */
    private String unionId;
    /****
     * OpenId
     */
    private String openId;
}