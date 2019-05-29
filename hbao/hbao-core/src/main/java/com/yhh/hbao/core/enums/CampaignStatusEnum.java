package com.yhh.hbao.core.enums;

/**
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-10 下午2:13
 **/

public enum CampaignStatusEnum {

    IN_STOPPED(0), //已停止
    IN_PROGRESS(1), //进行中
    IN_COMPLETE(2); //已结束

    private int value;

    CampaignStatusEnum(int value) {
        this.value = value;
    }

    public int getStatus() {
        return value;
    }
}
