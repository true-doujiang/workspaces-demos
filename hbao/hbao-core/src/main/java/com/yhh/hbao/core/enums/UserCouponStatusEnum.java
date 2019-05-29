package com.yhh.hbao.core.enums;


/**
 * 用户卡券状态枚举
 *
 * @author Xiaolong Li
 * @E-Mail lixl@163.com
 * @create 2018-05-14
 **/
public enum UserCouponStatusEnum {

    USER_COUPON_INIT(0), //默认初始化
    USER_COUPON_ACTIVATED(1), //已激活
    USER_COUPON_USED(2), //已使用
    USER_COUPON_EXP(3); //已过期

    private Integer value;

    UserCouponStatusEnum(final Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}