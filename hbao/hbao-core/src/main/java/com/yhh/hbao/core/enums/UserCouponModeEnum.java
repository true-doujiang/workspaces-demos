package com.yhh.hbao.core.enums;


/**
 * 用户卡券状态枚举
 *
 * @author Xiaolong Li
 * @E-Mail lixl@163.com
 * @create 2018-05-14
 **/
public enum UserCouponModeEnum {
    DEFAULT(0),         // 默认
    ISSUE(1),           // 发放
    RECEIVE(2),         // 领取
    PROMOTION(3);       // 促销

    private Integer value;

    UserCouponModeEnum(final Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static void main(String[] args) {
        UserCouponModeEnum  e= UserCouponModeEnum.valueOf("DEFAULT");
        System.out.println(e.getValue());
    }
}