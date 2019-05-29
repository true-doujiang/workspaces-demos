package com.yhh.hbao.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 实体父类BaseDataEntity
 *
 * @author tqj
 * @since 2017-12-04 11:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDataReqVo extends BaseReqVo implements Serializable {

}
