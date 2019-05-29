package com.yhh.hbao.web.vo;

import com.yhh.hbao.web.model.BaseDataReqVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class CampaignStatusVo extends BaseDataReqVo implements Serializable{
    private Boolean status;
}
