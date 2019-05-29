package com.yhh.hbao.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http 请求工具
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-12 上午10:41
 **/
public class HttpUtil {
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    public static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static <T> T doGet(String url, Map<String, String> params,Class<T> clazz){
        return doGet(url, params,CHARSET,clazz);
    }
    public static <T> T doPost(String url, Map<String, String> params,Class<T> clazz){
        return doPost(url, params,CHARSET,clazz);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static <T> T doGet(String url,Map<String,String> params,String charset,Class<T> clazz){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return JSON.parseObject(result,clazz);
        } catch (Exception e) {
            LOGGER.error("HTTP GET ERROR",e);
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static <T> T doPost(String url,Map<String,String> params,String charset,Class<T> clazz){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,charset));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return JSON.parseObject(result,clazz);
        } catch (Exception e) {
            LOGGER.error("HTTP POST ERROR",e);
        }
        return null;
    }
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param jsonRow	请求的参数
     * @param charset	编码格式
     * @return	页面内容
     */
    public static <T> T doPostRow(String url,String jsonRow,String charset,Class<T> clazz){
        if(StringUtils.isEmpty(url)){
            return null;
        }
        try {

            HttpPost httpPost = new HttpPost(url);
            if(!StringUtils.isEmpty(jsonRow)){
                httpPost.setEntity(new StringEntity(jsonRow,charset));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return JSON.parseObject(result,clazz);
        } catch (Exception e) {
            LOGGER.error("HTTP POST ROW ERROR",e);
        }
        return null;
    }


}