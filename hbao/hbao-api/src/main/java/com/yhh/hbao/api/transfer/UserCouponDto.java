package com.yhh.hbao.api.transfer;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

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
public class UserCouponDto extends BaseDto implements Serializable {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 券ID
     */
    private Long couponId;
    /**
     * 领取id
     */
    private Long receiveId;
    /****
     * 活动ID
     */
    private Long campaignId;
    /**
     * 所属用户ID
     */
    private Long userId;
    /**
     * 获取时间
     */
    private Date getTime;
    /**
     * 获取方式
     */
    private Integer getType;
    /**
     * 劵码
     */
    private String couponCode;

    private String city;
    private String headIcon;
    private Long mobile;
    private String nickName;
    private Integer status;
    private String statusDesc;
    private Date gettime;
    private Integer price;

    private String campaignName;
    /**
     * 活动信息
     */
    private CampaignDto campaignDto;
    /**
     * 卡券信息
     */
    private CouponInfoDto couponInfoDto;
    /**
     * 领取记录
     */
    private ReceiveLogsDto receiveLogsDto;

    private Integer couponType;
    private String couponTypeDesc;
}
