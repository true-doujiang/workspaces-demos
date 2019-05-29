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
 * 拆券记录
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(include=NON_NULL)
public class BrokenLogsVo extends BaseDataReqVo implements Serializable{

            /**
    * 自增id
    */
    private Long id;
    /**
    * 拆取用户ID
    */
    private Long userId;
    /**
    * 拆取用户昵称
    */
    private String niceName;
    /**
    * 领取记录Id
    */
    private Long receiveLogsId;
    /**
    * 活动ID
    */
    private Long campaignId;
    /**
    * 拆取时间
    */
    private Date brokenTime;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;


}
