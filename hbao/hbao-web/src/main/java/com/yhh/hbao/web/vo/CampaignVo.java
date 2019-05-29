package com.yhh.hbao.web.vo;

import com.yhh.hbao.web.model.BaseDataReqVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

/**
 * <p>
 * 活动信息
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(include=NON_NULL)
public class CampaignVo extends BaseDataReqVo implements Serializable{

            /**
    * 自增ID
    */
    private Long id;

    private String name;
    /**
    * 活动开始时间
    */
    private String validBeginTime;
    /**
    * 活动结束时间
    */
    private String validEndTime;
    /**
    * 领取限制 1:无限制 2:有限制
    */
    private Integer receiveType;
    /**
    * 每人每天领取限制 默认:0
    */
    private Integer userDayCount;
    /**
    * 每人累计领取限制 默认:0
    */
    private Integer userAllCount;
    /**
    * 卡券类型 1:红包 2:代金 3:兑换
    */
    private Integer couponType;
    /**
    * 面额:0 单位:分
    */
    private Integer price;
    /**
    * 礼品名称
    */
    private String giftName;
    /**
    * 优先等级:0
    */
    private Integer level;
    /**
    * 拆取次数:0
    */
    private Integer brokenCount;
    /**
    * 有效期类型:1:无 2:有
    */
    private Integer validTimeType;
    /**
    * 有效期小时数
    */
    private Integer validHour;
    /**
    * 有效期开始时间
    */
    private String beginTime;
    /**
    * 有效期结束时间
    */
    private String endTime;
    /**
    * 发行量默认0
    */
    private Integer issueCount;

    /****
     * 已使用数量
     */
    private Integer usedCount;
    /**
    * 权重默认0
    */
    private Integer weight;
    /**
    * 使用规则
    */
    private String useRules;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;
    /**
    * 消费是否限制 1:无 2:有
    */
    private Integer consumerLimitType;
    /**
    * 消费满多少可用
    */
    private Integer consumerCount;

    private Integer status;


}
