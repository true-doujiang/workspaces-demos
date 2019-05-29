package com.yhh.hbao.api.transfer;

import lombok.Data;

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
public class ReceiveLogsDtoRes extends BaseDto implements Serializable {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 领取活动ID
     */
    private Long campaignId;

    /****
     * 消费限制 有无门槛
     */
    private Integer consumerLimitType;
    /**
     * 领取时间
     */
    private Date getTime;

    /****
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    private String nickName;

    private Long mobile;

    private String city;

    private String province;

    private String headIcon;

    private Integer price;

    private Integer brokenCount;

    private Integer couponType;

    private Integer validHour;

    private Long couponId;

}
