package com.yhh.hbao.core.enums;

/**
 * 登录枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-14 下午6:43
 **/
public enum  LoginEnum {


    TOC(1,"TOC登录"),
    TOB(2,"TOB登录"),
    SYS(3,"系统扥估");

    /****
     * code 码
     */
    private Integer code;
    /****
     * 说明详情
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

    LoginEnum(Integer code, String desc) {
        this.code=code;
        this.desc=desc;

    }
}