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
 * 领取记录
 * </p>
 *
 * @author yhh
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("receive_logs")
public class ReceiveLogs extends Model<ReceiveLogs> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 领取活动ID
     */
    @TableField("campaign_id")
    private Long campaignId;
    /****
     * 消费限制 有无门槛
     */
    @TableField("consumer_limit_type")
    private Integer consumerLimitType;
    /**
     * 领取时间
     */
    @TableField("get_time")
    private Date getTime;

    /***
     * 过期时间
     */
    @TableField("invalid_time")
    private Date invalidTime;

    /****
     * 状态
     */
    private Integer status;
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
