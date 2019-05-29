package com.yhh.hbao.web.model.weixin;

import lombok.Data;

import java.io.Serializable;

/**
 * package: com.weimob.voteplaza.service.model
 * describe:  微信 keyword
 * creat_user: yhh
 * e-mail: yhh@weimob.com
 * creat_date: 9/20/17
 * creat_time: 2:26 PM
 **/
@Data
public class Keyword implements Serializable{

    private String value;

    private String color;

    public Keyword(){

    }
    public Keyword(String value){
        this.setValue(value);
    }
    public Keyword(String color,String value){
        this.setValue(value);
        this.setColor(color);
    }
}
