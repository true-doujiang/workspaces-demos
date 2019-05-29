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

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "campaign")
public class Campaign extends Model<Campaign> {


    private static final long serialVersionUID=1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    @TableField("name")
    private String name;
    /**
     * 活动开始时间
     */
    @TableField("valid_begin_time")
    private Date validBeginTime;
    /**
     * 活动结束时间
     */
    @TableField("valid_end_time")
    private Date validEndTime;
    /**
     * 领取限制 1:无限制 2:有限制
     */
    @TableField("receive_type")
    private Integer receiveType;
    /**
     * 每人每天领取限制 默认:0
     */
    @TableField("user_day_count")
    private Integer userDayCount;
    /**
     * 每人累计领取限制 默认:0
     */
    @TableField("user_all_count")
    private Integer userAllCount;
    /**
     * 卡券类型 1:红包 2:代金 3:兑换
     */
    @TableField("coupon_type")
    private Integer couponType;
    /**
     * 面额:0 单位:分
     */
    private Integer price;
    /**
     * 礼品名称
     */
    @TableField("gift_name")
    private String giftName;
    /**
     * 优先等级:0
     */
    private Integer level;
    /**
     * 拆取次数:0
     */
    @TableField("broken_count")
    private Integer brokenCount;
    /**
     * 有效期类型:1:无 2:有
     */
    @TableField("valid_time_type")
    private Integer validTimeType;
    /**
     * 有效期小时数
     */
    @TableField("valid_hour")
    private Integer validHour;
    /**
     * 有效期开始时间
     */
    @TableField("begin_time")
    private Date beginTime;
    /**
     * 有效期结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 发行量默认0
     */
    @TableField("issue_count")
    private Integer issueCount;

    /****
     * 已使用数量
     */
    @TableField("used_count")
    private Integer usedCount;
    /**
     * 权重默认0
     */
    private Integer weight;
    /**
     * 使用规则
     */
    @TableField("use_rules")
    private String useRules;
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
    /**
     * 消费是否限制 1:无 2:有
     */
    @TableField("consumer_limit_type")
    private Integer consumerLimitType;
    /**
     * 消费满多少可用
     */
    @TableField("consumer_count")
    private Integer consumerCount;

    private Integer status;

    /**
     * 是否已经删除
     */
    @TableField("is_delete")
    private Integer isDelete;

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
