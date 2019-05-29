package com.yhh.hbao.web.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 拆券记录
 * </p>
 * @author yhh
 * @since 2018-05-18
 */
@Data
public class ReceiveLogsParamVo {
    /**
    * 领取记录Id
    */
    @NotNull(message = "领取记录ID不能为空")
    private Long receiveLogsId;

    /**
     * 领取记录Id
     */
    private Long campaignId;
}
