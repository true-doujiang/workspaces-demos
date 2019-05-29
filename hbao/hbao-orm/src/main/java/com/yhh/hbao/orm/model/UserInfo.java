package com.yhh.hbao.orm.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.yhh.hbao.core.utils.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 第三方唯一ID
     */
    @TableField("union_id")
    private String unionId;

    /****
     * openId
     */
    @TableField("open_id")
    private String openId;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 用户头像地址
     */
    @TableField("head_icon")
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
    @TableField("register_time")
    private Date registerTime;
    /**
     * 最后一次登录时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;
    /**
     * 用户的语言
     */
    @TableField("use_language")
    private String useLanguage;
    /**
     * 用户状态0:正常 1:关闭
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_at")
    private Date createAt;
    /**
     * 修改时间
     */
    @TableField("update_at")
    private Date updateAt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /****
     *转Dto对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toDto(Class<T> clazz) {
        return BeanUtils.copyProperties(this, clazz);
    }
}
