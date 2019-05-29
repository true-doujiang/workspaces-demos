package com.yhh.hbao.core.enums;

/**
 * 卡券类型枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:02
 **/
public enum CouponTypeEnum {
    RED(1),//红包
    EXCHANGE_COUPON(2), // 代金劵
    CASH_COUPON(3);     // 兑换劵

    private Integer value;

    CouponTypeEnum(final Integer value){
        this.value=value;
    }

    public Integer getValue() {
        return value;
    }
}