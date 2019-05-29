package com.yhh.hbao.core.validator;

/**
 * Created by yangjj.
 *
 * @DATE 2016/4/14 - 11:28
 * @company WeiMob
 * @description 定义校验的类型
 */
public enum ValidateType {
    /**
     * 1.参数非空
     * 2.参数不可以为空或""
     * 3.自定义校验
     * 4.容器类校验
     */
    NOT_NULL, NOT_EMPTY, CUSTOM, CONTAIN
}
