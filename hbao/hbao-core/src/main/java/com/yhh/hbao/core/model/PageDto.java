package com.yhh.hbao.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by @author yhh.
 *
 * @DATE 2017/12/29 - 14:47
 * @company dianda
 * @description 基础页面数据dto传输对象
 */
@Data
public class PageDto implements Serializable {
    /**
     * 第几页
     */
    private Integer page = 1;
    /**
     * 每页多少条
     */
    private Integer pageSize = 10;
    /**
     * 总条数
     */
    private Integer totalCount = 0;
    /**
     * 总页数
     */
    private Integer pageCount = 0;





}
