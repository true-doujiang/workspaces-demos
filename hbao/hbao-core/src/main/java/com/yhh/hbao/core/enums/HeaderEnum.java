package com.yhh.hbao.core.enums;

/**
 * @author yhh
 * @date 1/13/17 4:50 PM
 */
public enum HeaderEnum {
    AccessControlAllowOrigin("Access-Control-Allow-Origin"),
    AccessControlAllowMethods("Access-Control-Allow-Methods"),
    AccessControlMaxAge("Access-Control-Max-Age"),
    AccessControlAllowHeaders("Access-Control-Allow-Headers"),
    AccessControlAllowCredentials("Access-Control-Allow-Credentials");
    public String value;

    HeaderEnum(String value){
        this.value=value;
    }

}
