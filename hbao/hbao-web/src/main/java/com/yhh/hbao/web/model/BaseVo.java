package com.yhh.hbao.web.model;

import com.yhh.hbao.core.utils.BeanUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础vo
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-12 下午6:26
 **/
@Data
public class BaseVo implements Serializable {
    /****
     *转DTO对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toDto(Class<T> clazz){
        return BeanUtils.copyProperties(this,clazz);
    }
}