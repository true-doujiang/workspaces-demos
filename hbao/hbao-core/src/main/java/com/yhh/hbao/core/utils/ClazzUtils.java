package com.yhh.hbao.core.utils;

import java.lang.reflect.Field;

public class ClazzUtils {
    /**
     * 获取申明的clazz 的字段
     *
     * @param targetClazz 目标clazz
     * @param fieldName   字段名称
     * @return 字段
     */
    public static Field getDeclaredField(Class<?> targetClazz, String fieldName) {
        try {
            return targetClazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            LogUtils.error(targetClazz, "目标clazz 获取 filedName错误", e);
        }
        return null;
    }
}
