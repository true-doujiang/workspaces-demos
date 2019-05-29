package com.yhh.hbao.core.annotation;

import java.lang.annotation.*;

/**
 * Created by yangjj.
 *
 * @DATE 2018/1/17 - 16:10
 * @company WeiMob
 * @description json序列化设置  用于string 类型 转json数据操作
 * <p/>
 * <p>
 * 此注解只能在string 类型的字段上 使用
 * </p>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerializable {
    /**
     * 要求class数据设置
     *
     * @return 目标json的class 配置
     */
    Class<?> requireClass();

}
