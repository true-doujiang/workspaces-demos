package com.yhh.hbao.core.utils;

import java.lang.annotation.*;

/**
 * Created by yangjj.
 *
 * @DATE 2016/6/22 - 9:40
 * @company WeiMob
 * @description map拷贝设置的名字
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertiesName {
    /**
     * 名称
     *
     * @return
     */
    String name();


}
