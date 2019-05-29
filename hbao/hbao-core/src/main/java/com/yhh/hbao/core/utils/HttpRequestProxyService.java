package com.yhh.hbao.core.utils;

import java.util.Map;

/**
 * Created by yangjj.
 *
 * @DATE 2016/5/18 - 17:14
 * @company WeiMob
 * @description 远程http请求的服务  基础的http服务
 */

public interface HttpRequestProxyService {

    /**
     * 发送http post json请求
     *
     * @param requestUrl 请求URL地址
     * @param request    请求对象
     * @return 返回真实的数据
     */
    String httpPostJson(String msgId, String requestUrl, Object request);

    /**
     * 发送http post json请求
     *
     * @param requestUrl 请求URL地址
     * @param request    请求对象
     * @param timeOut    请求超时时间
     * @return 返回真实的数据
     */
    String httpPostJson(String msgId, String requestUrl, Object request, int timeOut);


    /**
     * 发送http post json请求
     *
     * @param requestUrl 请求URL地址
     * @param jsonStr    jsonStr请求对象
     * @return 返回真实的数据
     */
    String httpPostJsonStr(String msgId, String requestUrl, String jsonStr);

    /**
     * 发送http post 参数请求
     *
     * @param requestUrl 请求URL地址
     * @param request    请求对象
     * @return 返回真实数据
     */
    String httpPost(String msgId, String requestUrl, Object request);


    /**
     * 发送http get 参数请求
     *
     * @param requestUrl 请求URL地址
     * @param request    请求对象
     * @return 返回真实数据
     */
    String httpGet(String msgId, String requestUrl, Object request);

    /**
     * 发送http GET请求字符串
     *
     * @param msgId      全局消息编号
     * @param requestUrl 请求Url
     * @param requestStr 请求字符串
     * @return 响应数据
     */
    String httpGetStr(String msgId, String requestUrl, String requestStr);

    /**
     * 发送http get post数据请求
     *
     * @param msgId      消息编号
     * @param requestUrl 请求url
     * @param paramsMap  参数map对象数据
     * @return 响应数据
     */
    String httpGetMap(String msgId, String requestUrl, Map<String, Object> paramsMap);


    /**
     * 发送http get 数据请求
     *
     * @param msgId      消息编号
     * @param requestUrl 请求url
     * @param paramsMap  参数map对象数据
     * @param headerMap  请求头部信息
     * @return 响应数据
     */
    String httpGetMap(String msgId, String requestUrl, Map<String, Object> paramsMap, Map<String, String> headerMap);

    /**
     * 发送 http  POST map请求数据
     *
     * @param msgId      全局消息编号
     * @param requestUrl 请求url
     * @param paramsMap  请求map对象
     * @return 响应数据
     */
    String httpPostMap(String msgId, String requestUrl, Map<String, Object> paramsMap);

}
