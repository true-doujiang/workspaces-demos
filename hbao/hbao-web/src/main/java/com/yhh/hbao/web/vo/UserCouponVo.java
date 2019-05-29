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
 * 用户卡券信息
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(include=NON_NULL)
public class UserCouponVo extends BaseDataReqVo implements Serializable{

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


}
