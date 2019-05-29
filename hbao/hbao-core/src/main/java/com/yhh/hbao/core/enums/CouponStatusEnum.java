package com.yhh.hbao.core.enums;

/**
 * 卡券状态枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:13
 **/
public enum CouponStatusEnum {
    NO_ACTIVATION(0),      //  未激活
    LOCK(1),     // 已锁定
    BIND(2),    //已绑定
    INVALID(3),    //已失效
    USED(4),    //已使用
    OVERDUED(5); //已过期


    private Integer value;

    CouponStatusEnum(final Integer value){
        this.value=value;
    }

    public Integer getValue() {
        return value;
    }
}