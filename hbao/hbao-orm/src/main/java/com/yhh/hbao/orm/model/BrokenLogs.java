package com.yhh.hbao.orm.model;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.yhh.hbao.core.utils.BeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@TableName("broken_logs")
public class BrokenLogs extends Model<BrokenLogs> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 拆取用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 拆取用户昵称
     */
    @TableField("nice_name")
    private String niceName;
    /**
     * 领取记录Id
     */
    @TableField("receive_logs_id")
    private Long receiveLogsId;
    /**
     * 活动ID
     */
    @TableField("campaign_id")
    private Long campaignId;
    /**
     * 拆取时间
     */
    @TableField("broken_time")
    private Date brokenTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /****
     *转Dto对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toDto(Class<T> clazz) {
        return BeanUtils.copyProperties(this, clazz);
    }
}
