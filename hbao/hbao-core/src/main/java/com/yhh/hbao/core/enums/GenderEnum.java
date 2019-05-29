package com.yhh.hbao.core.enums;

/**
 * 用户性别枚举
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:14
 **/
public enum GenderEnum {

    UNKONW(0,"未知"),
    MAN(1,"男"),
    WOMAN(2,"女");

    /*****
     * code 码
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

    GenderEnum(Integer code, String desc) {
        this.code=code;
        this.desc=desc;
    }
    public static Integer getCode(String desc){

        for(GenderEnum genderEnum:GenderEnum.values()){
            if(genderEnum.getDesc().equals(desc)){
                return genderEnum.getCode();
            }
        }
        return GenderEnum.UNKONW.getCode();

    }
    public static String getDesc(Integer code){
        for(GenderEnum genderEnum:GenderEnum.values()){
            if(genderEnum.getCode().equals(code)){
                return genderEnum.getDesc();
            }
        }
        return GenderEnum.UNKONW.getDesc();

    }
}