package com.yhh.hbao.web.model;


import lombok.Data;

import java.io.Serializable;

/**
 * 实体父类BaseEntity
 *
 * @author tqj
 * @since 2017-12-04 11:41
 */
@Data
public class BaseReqVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Long id;

    /**
     * 应用来源
     */
    protected String appIdentifier;

    /**
     * 请求reqId
     */
    protected String reqId;


}
