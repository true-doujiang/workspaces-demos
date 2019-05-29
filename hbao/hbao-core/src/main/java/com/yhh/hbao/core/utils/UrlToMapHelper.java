package com.yhh.hbao.core.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangjj.
 *
 * @DATE 2017/8/12 - 11:03
 * @company WeiMob
 * @description url 参数转化成map对象帮助类
 */
public class UrlToMapHelper {
    /**
     * url参数转化成map对象
     *
     * @param urlParam url参数
     * @return map对象
     */
    public static Map<String, Object> converter(String urlParam) {
        Map<String, Object> reqParam = new HashMap<>();
        String[] results = urlParam.split("&");
        for (String data : results) {
            String[] keys = data.split("=");
            if (keys.length == 2) {
                reqParam.put(keys[0], keys[1]);
            }
        }
        return reqParam;
    }


    public static Map<String, Object> converter(Map<String, String[]> httpGetParam) {
        if (Utils.isBlank(httpGetParam)) return null;
        Map<String, Object> reqParam = new HashMap<>();
        for (Map.Entry<String, String[]> entry : httpGetParam.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (value != null && value.length > 1) {
                reqParam.put(key, Arrays.asList(value));
            } else if (value != null) {
                reqParam.put(key, value[0]);
            }
        }
        return reqParam;
    }


    /**
     * 读取流数据
     *
     * @param in 请求流
     * @return 流数据
     */
    public static String readInputStream(InputStream in) {
        if (in == null) return null;
        try {
            StringBuilder requestBody = new StringBuilder();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                requestBody.append(new String(buf, 0, len, "utf-8"));
            }
            return requestBody.toString();
        } catch (IOException e) {
            LogUtils.error(UrlToMapHelper.class, "流读取异常失败", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LogUtils.error(UrlToMapHelper.class, "流关闭异常", e);
            }
        }
        return null;
    }


}
