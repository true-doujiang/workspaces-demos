package com.yhh.hbao.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yhh.
 *
 * @DATE 2018/1/18 - 16:01
 * @description thread local 工具类设置
 */
public class ThreadLocalUtils {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    public static void set(String key, Object value) {
        threadLocal.get().put(key, value);
    }


    public static Object get(String key) {
        return threadLocal.get().get(key);
    }


}
