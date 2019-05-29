package com.yhh.hbao.api.transfer;


import com.yhh.hbao.core.utils.BeanUtils;
import lombok.Data;
import java.io.Serializable;

/**
 * 基础DTO模型
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-12 下午6:25
 **/
@Data
public class BaseDto implements Serializable {

    /****
     *转Vo对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toVo(Class<T> clazz) {
        return BeanUtils.copyProperties(this, clazz);
    }

    /****
     *转实体模型
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toModel(Class<T> clazz) {
        return BeanUtils.copyProperties(this, clazz);
    }
}