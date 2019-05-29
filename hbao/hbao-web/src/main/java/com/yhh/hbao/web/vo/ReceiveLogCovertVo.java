package com.yhh.hbao.web.vo;

import com.yhh.hbao.web.model.BaseDataReqVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

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

public class ReceiveLogCovertVo extends BaseDataReqVo implements Serializable{

    /**
    * 以兑换数量
    */
    private Long count;
    /**
    * 已兑换部分用户名称
    */
    private List<String> batches;

}
