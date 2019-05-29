package com.yhh.hbao.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResultAndPage implements Serializable {

    private Object data;//返回data
    private PageDto pagination;//返回分页信息


    public ResultAndPage(Object data, PageDto pagination) {
        this.data = data;
        this.pagination = pagination;
    }
}
