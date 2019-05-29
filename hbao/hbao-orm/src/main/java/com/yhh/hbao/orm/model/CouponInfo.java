package com.yhh.hbao.orm.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yhh.hbao.core.utils.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 卡券信息
 * </p>
 * @author yhh
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("coupon_info")
public class CouponInfo extends Model<CouponInfo> {

    private static final long serialVersionUID=1L;

            /**
    * 自增ID
    */
                    private Long id;
    /**
    * 活动Id
    */
    @TableField("campaign_id")
    private Long campaignId;
    /**
    * 券码
    */
    @TableField("coupon_code")
    private String couponCode;
    /**
    * 失效时间
    */
    @TableField("invalid_time")
    private Date invalidTime;
    /**
    * 获取时间
    */
    @TableField("get_time")
    private Date getTime;
    /**
    * 来源:1:自主生成 2:第三方生成
    */
    private Integer source;
    /**
    * 卡券状态 0:未激活 1:已激活 2:已失效 3:已使用 4:已过期
    */
    private Integer status;
    /**
    * 是否已核销 0:否 1:是
    */
    @TableField("is_verification")
    private Integer isVerification;
    /**
    * 创建时间
    */
    @TableField("create_time")
    private Date createTime;
    /**
    * 修改时间
    */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal(){
            return this.id;
     }

    /****
     *转Dto对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toDto(Class<T> clazz){
        return BeanUtils.copyProperties(this,clazz);
    }

}
