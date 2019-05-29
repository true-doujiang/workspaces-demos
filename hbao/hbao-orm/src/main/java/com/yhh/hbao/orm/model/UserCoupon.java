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
 * 用户卡券信息
 * </p>
 *
 * @author yhh
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_coupon")
public class UserCoupon extends Model<UserCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 券ID
     */
    @TableField("coupon_id")
    private Long couponId;
    /**
     * 领取id
     */
    @TableField("receive_id")
    private Long receiveId;

    /****
     * 活动ID
     */
    @TableField("campaign_id")
    private Long campaignId;
    /**
     * 所属用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 获取时间
     */
    @TableField("get_time")
    private Date getTime;
    /**
     * 获取方式
     */
    @TableField("get_type")
    private Integer getType;

    /**
     * 失效时间
     */
    @TableField("invalid_time")
    private Date invalidTime;

    private Integer status;
    /**
     * 劵码
     */
    @TableField("coupon_code")
    private String couponCode;

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
