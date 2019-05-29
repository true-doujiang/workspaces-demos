package com.yhh.hbao.core.utils;

import lombok.Data;

/**
 * Created by yangjj.
 *
 * @DATE 2018/2/5 - 10:39
 * @company WeiMob
 * @description beanDiff参数不同的数据
 */
@Data
public class BeanDiff {
    /**
     * 原值
     */
    private Object sourceValue;

    /**
     * 目标值
     */
    private Object targetValue;


    public static BeanDiff build(Object sourceValue, Object targetValue) {
        BeanDiff beanDiff = new BeanDiff();
        beanDiff.setSourceValue(sourceValue);
        beanDiff.setTargetValue(targetValue);
        return beanDiff;
    }
}
