package com.yhh.hbao.web.interceptor;

import com.yhh.hbao.api.transfer.UserInfoDto;
import com.yhh.hbao.core.enumerate.CouponBizExceptionEnum;
import com.yhh.hbao.core.redis.RedisClientUtil;
import com.yhh.hbao.core.utils.MD5Encrypt;
import com.yhh.hbao.web.model.ResultResponse;
import com.yhh.hbao.web.utils.TokenUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 请求拦截
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-15 下午3:40
 **/

public class ModifyIntereptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisClientUtil redisClientUtil;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE_NAME = "content-type";
    private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/json";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        String method = request.getMethod();
        if(method.equalsIgnoreCase(HttpMethod.GET.name())){
            return true;
        }
        String url = request.getRequestURI();
        String key = userInfoDto.getUnionId() + '.' + method + '.' + url;
        if(redisClientUtil.exists(key)){
            handleResponse(response,ResultResponse.fail(CouponBizExceptionEnum.REQUEST_IS_TOO_TRIVIAL.getErrorCode(),CouponBizExceptionEnum.REQUEST_IS_TOO_TRIVIAL.getErrorMessage()));
            return false;
        }
        redisClientUtil.set(key, "running", 3);
        return true;
    }

    /****
     *  回写结果
     * @param response
     * @param o
     */
    private void handleResponse(HttpServletResponse response,Object o) {
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            response.setCharacterEncoding(DEFAULT_CHARSET);
            response.setHeader(DEFAULT_CONTENT_TYPE_NAME, DEFAULT_CONTENT_TYPE_VALUE);
            printWriter.write(JSON.toJSONString(o));
        } catch (Exception e) {
        } finally {
            if (null != printWriter) {
                printWriter.flush();
                printWriter.close();
            }

        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        UserInfoDto userInfoDto = TokenUtils.getToken().getUserInfoDto();
        String url = request.getRequestURI();
        String method = request.getMethod();
        String key = userInfoDto.getUnionId() + '.' + method + '.' + url;
        if(redisClientUtil.exists(key)){
            redisClientUtil.del(key);
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}