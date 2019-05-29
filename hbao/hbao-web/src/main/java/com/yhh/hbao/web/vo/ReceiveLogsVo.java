package com.yhh.hbao.web.vo;

import com.yhh.hbao.api.transfer.ReceiveLogsDto;
import com.yhh.hbao.core.utils.StringUtils;
import com.yhh.hbao.web.model.BaseDataReqVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

/**
 * <p>
 * 领取记录
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(include=NON_NULL)
public class ReceiveLogsVo extends BaseDataReqVo implements Serializable{

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
     * 是否兑换
     */
    private Integer status;

    /****
     * 手机号
     */
    private Long mobile;
    /****
     * 微信昵称
     */
    private String nickName;
    /****
     * 城市
     */
    private String city;
    /****
     * 卡券类型 1:红包 2:代金 3:兑换'
     */
    private Integer couponType;

    private String keyword;

    public ReceiveLogsDto toDto(){
        ReceiveLogsDto dto = new ReceiveLogsDto();
        BeanUtils.copyProperties(this,dto);
        String keyword = this.getKeyword();
        if (!StringUtils.isEmpty(keyword)){
            dto.setNickName(keyword);
            try{
                long mobile = Long.parseLong(keyword);
                //如果没有异常说明可能是是电话号
                dto.setMobile(mobile);
            } catch (NumberFormatException e){
                //do nothing
            }
        }
        return dto;
    }


}
