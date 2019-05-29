package com.yhh.hbao.core.enums;


/**
 * 领取状态枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-19 下午1:09
 **/
public enum ToBUserCouponEnums {

    USER_COUPON_RECEIVE(0,"已领取"),
    USER_COUPON_BROKENED(1,"已拆开"),
    USER_COUPON_EXCHANGED(2,"已兑换"),
    USER_COUPON_OVERDUE(3,"已过期");

    /****
     * code
     */
    private Integer code;
    /****
     * 说明
     */
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ToBUserCouponEnums(Integer code, String desc) {

        this.code=code;
        this.desc=desc;
    }
    public static Integer getCode(String desc){

        for(ToBUserCouponEnums receiveOpenEnums:ToBUserCouponEnums.values()){
            if(receiveOpenEnums.getDesc().equals(desc)){
                return receiveOpenEnums.getCode();
            }
        }
        return null;
    }
    public static String getDesc(Integer code){

        for(ToBUserCouponEnums receiveOpenEnums:ToBUserCouponEnums.values()){
            if(receiveOpenEnums.getCode().equals(code)){
                return receiveOpenEnums.getDesc();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String a = ToBUserCouponEnums.getDesc(1);
        System.out.println(a);
    }
}