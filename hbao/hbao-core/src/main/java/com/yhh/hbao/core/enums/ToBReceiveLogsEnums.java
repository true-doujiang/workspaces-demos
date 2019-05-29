package com.yhh.hbao.core.enums;


/**
 * 领取状态枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-19 下午1:09
 **/
public enum ToBReceiveLogsEnums {

    RECEIVELOGS_RECEIVE(1,"已领取"),
    RECEIVELOGS_BROKENED(2,"已拆开"),
    RECEIVELOGS_EXCHANGED(3,"已兑换"),
    RECEIVELOGS_OVERDUE(4,"已过期");

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

    ToBReceiveLogsEnums(Integer code, String desc) {

        this.code=code;
        this.desc=desc;
    }
    public static Integer getCode(String desc){

        for(ToBReceiveLogsEnums receiveOpenEnums:ToBReceiveLogsEnums.values()){
            if(receiveOpenEnums.getDesc().equals(desc)){
                return receiveOpenEnums.getCode();
            }
        }
        return null;
    }
    public static String getDesc(Integer code){

        for(ToBReceiveLogsEnums receiveOpenEnums:ToBReceiveLogsEnums.values()){
            if(receiveOpenEnums.getCode().equals(code)){
                return receiveOpenEnums.getDesc();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String a = ToBReceiveLogsEnums.getDesc(1);
        System.out.println(a);
    }
}