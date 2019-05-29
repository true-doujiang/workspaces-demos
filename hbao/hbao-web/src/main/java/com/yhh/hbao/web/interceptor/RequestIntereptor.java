package com.yhh.hbao.web.interceptor;

import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.redis.RedisClientUtil;
import com.yhh.hbao.core.utils.MD5Encrypt;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.model.weixin.TokenInfo;
import com.yhh.hbao.web.utils.TokenUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 请求拦截
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-15 下午3:40
 **/

public class RequestIntereptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisClientUtil redisClientUtil;

    /** http相关参数 **/
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE_NAME = "content-type";
    private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/json";

    private static final String AUTHORIZATION_KEY = "Authorization";

    /**
     * 数据校验Key值
     */
    private static final String MD5_KEY = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return verifySignatureToken(request,response);

    }

    /****
     *  token 校验
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private Boolean verifySignatureToken(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String token = request.getHeader(AUTHORIZATION_KEY);
        if(StringUtils.isEmpty(token)||!redisClientUtil.exists(token)){
            handleResponse(response,ResultResponse.fail(CouponBizExceptionEnum.AUTH_USER_LOGIN_TOKEN_IS_NULL.getErrorCode(),CouponBizExceptionEnum.AUTH_USER_LOGIN_TOKEN_IS_NULL.getErrorMessage()));
            return false;
        }
        TokenUtils.putToken(JSON.parseObject(redisClientUtil.get(token),TokenInfo.class));
        return true;
    }

    /****
     *  回写结果
     * @param response
     * @param o
     */
    private void handleResponse(HttpServletResponse response,Object o){
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
//            response.setCharacterEncoding(DEFAULT_CHARSET);
            response.setHeader(DEFAULT_CONTENT_TYPE_NAME, DEFAULT_CONTENT_TYPE_VALUE);
            printWriter.write(JSON.toJSONString(o));
        }catch (Exception e){

        }finally {
            if(null!=printWriter){
                printWriter.flush();
                printWriter.close();
            }

        }
    }
    /**
     * 数据防篡改验证
     */
    private Boolean verifySignature(String parameterInput) {
        Boolean isSign = true;
        if (!StringUtils.isEmpty(parameterInput)) {
            Map<String, Object> dataMap = new HashedMap();
            dataMap = JSON.parseObject(parameterInput, HashedMap.class);

            isSign = false;
            if (parameterInput.contains("\"_sign_\"")) { // 是否包含签名字段
                String sign = (String) dataMap.get("_sign_");
                int beginIndex = parameterInput.indexOf("\"_sign_\"");
                int endIndex = beginIndex + sign.length() + ("\"_sign_\"").length() + 4;
                String preSign = parameterInput.substring(beginIndex, endIndex);
                String preMd5 = parameterInput.replaceFirst(preSign, "");
                String md5 = MD5Encrypt.md5(preMd5 + MD5_KEY);
                if (sign.equals(md5.toUpperCase())) {
                    isSign = true;
                }
            }
        }
        return isSign;
    }
}