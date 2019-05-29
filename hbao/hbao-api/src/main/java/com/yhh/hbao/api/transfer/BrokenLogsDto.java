package com.yhh.hbao.api.transfer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 拆券记录
 * </p>
 *
 * @author yhh
 *
 */
@Data
public class BrokenLogsDto extends BaseDto implements Serializable {

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
     * 拆取用户头像
     */
    private String headIcon;
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
