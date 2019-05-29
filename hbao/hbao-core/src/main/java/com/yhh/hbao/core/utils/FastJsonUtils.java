package com.yhh.hbao.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;

/**
 * Created by yangjj.
 *
 * @DATE 2016/3/25 - 9:20
 * @company WeiMob
 * @description
 */
public class FastJsonUtils {

    /**
     * fastJson 默认的配置
     */
    public static final SerializeConfig SERIALIZE_CONFIG;


    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        SERIALIZE_CONFIG.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 使用fastJson 将字符串转化成Bean
     *
     * @param sourceJsonStr 字符串
     * @param targetClass   目标bean的字节码
     * @param <T>           实现序列化接口的bean
     * @return 返回通过json转化的对象
     */
    public static <T> T jsonToBean(String sourceJsonStr, Class<T> targetClass) {
        if (sourceJsonStr == null || sourceJsonStr.length() == 0) return null;
        return JSON.parseObject(sourceJsonStr, targetClass);
    }


    /**
     * 使用fastjson 将字符串转化成带泛型的对象
     * ps:此方法可以循环去找到父节点数据
     *
     * @param sourceJsonStr 原json字符串
     * @param typeReference 泛型类型
     * @param <T>具体类型值
     * @return 具体类型对象数据
     */
    public static <T> T jsonToBean(String sourceJsonStr, TypeReference<T> typeReference) {
        if (sourceJsonStr == null || sourceJsonStr.length() == 0) return null;
        return JSON.parseObject(sourceJsonStr, typeReference);
    }

    /**
     * jsonStr转化成bean list数据
     *
     * @param sourceJsonStr json字符串
     * @param targetClass   目标对象class
     * @param <T>           目标对象
     * @return list 目标对象
     */
    public static <T> List<T> jsonToBeanList(String sourceJsonStr, Class<T> targetClass) {
        if (sourceJsonStr == null || sourceJsonStr.length() == 0) return null;
        return JSONArray.parseArray(sourceJsonStr, targetClass);
    }

    /**
     * 使用fastJson 将对象转化成Json字符串
     *
     * @param sourceBean 目标bean
     * @param <T>        实现序列化接口的bean
     * @return 返回json字符串
     */
    public static <T> String beanToJson(T sourceBean) {
        if (sourceBean == null) return null;
        return JSON.toJSONString(sourceBean, SERIALIZE_CONFIG);
    }

    /**
     * 此校验仅仅做基础的校验
     *
     * @param text 真实text值
     * @return 是否是json
     */
    public static boolean isJson(String text) {
        return !Utils.isEmpty(text) && ((text.startsWith("{") && text.endsWith("}")) || (text.startsWith("[") && text.endsWith("]")));
    }

    public static void main(String[] args) {
        String json = "{\"data\":\"1123\"}";
        boolean isJson = isJson(json);
        String html = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">";
        boolean isHtmlJson = isJson(html);
        System.out.println(isJson + ":" + isHtmlJson);
    }
}
