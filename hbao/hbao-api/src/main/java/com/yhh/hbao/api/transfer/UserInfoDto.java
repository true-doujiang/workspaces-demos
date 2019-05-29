package com.yhh.hbao.api.transfer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author yhh
 *
 */
@Data
public class UserInfoDto extends BaseDto implements Serializable {

    /**
     * 主键自增ID
     */
    private Long id;
    /**
     * 第三方唯一ID
     */
    private String unionId;
    /****
     * OpenId
     */
    private String openId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像地址
     */
    private String headIcon;
    /**
     * 手机号
     */
    private Long mobile;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 性别:默认:0 1:男 2:女
     */
    private Integer gender;
    /**
     * 用户注册时间
     */
    private Date registerTime;
    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;
    /**
     * 用户的语言
     */
    private String useLanguage;
    /**
     * 用户状态0:正常 1:关闭
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 修改时间
     */
    private Date updateAt;


}
