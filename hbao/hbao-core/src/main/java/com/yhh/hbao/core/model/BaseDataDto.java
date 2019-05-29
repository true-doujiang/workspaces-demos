package com.yhh.hbao.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体父类BaseDataDto
 *
 * @author yhh
 * @since 2017-12-04 11:41
 */
@Data
public class BaseDataDto implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建人ID
     */
    private Long createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人ID
     */

    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 是否已被删除   0未删除   1已删除
     */
    private Integer isDel;


}
