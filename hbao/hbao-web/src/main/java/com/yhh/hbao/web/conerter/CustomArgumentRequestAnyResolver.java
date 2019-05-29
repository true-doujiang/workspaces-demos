package com.yhh.hbao.web.conerter;

import com.yhh.hbao.web.anno.RequestAny;
import com.yhh.hbao.core.utils.*;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjj.
 *
 * @DATE 2017/7/3 - 10:39
 * @company WeiMob
 * @description 自定义参数拦截器
 * <p>解决  即在json 请求体内部设置数据  也可以post数据</p>
 * <p> get请求数据 也可以进行处理</p>
 * <p>
 * ps:  此注解方式的解析 仅仅支持 json  form表达相关的请求 混合表单提交的数据 不会被接收
 * 针对form表单的方式  进行数据判断
 * <p/>
 * 所有sign数据的校验 也在此方法进行处理  解析到数据 就直接对sign数据 校验
 * <p/>
 * <p/>
 * </p>
 */
public class CustomArgumentRequestAnyResolver implements HandlerMethodArgumentResolver {

    public static final String jsonRequestHead = "application/json; charset=utf-8";

    public static final String formReqHead = "multipart/form-data";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestAny.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //定义请求的参数类型数据设置
        Class<?> requestEntityClass = parameter.getParameterType();

        final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest.getMethod().equals(HttpMethod.GET.name())) {
            Map<String, String[]> paramMap = servletRequest.getParameterMap();
            Map<String, Object> transferParam = UrlToMapHelper.converter(paramMap);
            LogUtils.info(getClass(), "GET请求转换的map参数数据是:" + transferParam);
            Object targetObj = BeanUtils.copyMapToBean(requestEntityClass, transferParam);
            //返回空的对象
            if (targetObj == null) {
                return requestEntityClass.newInstance();
            }
            return targetObj;
        }

        //获取请求头信息
        String requestHeader = webRequest.getHeader("content-type");
        LogUtils.info(getClass(), "请求头部信息是:" + requestHeader);
        //如果是json 请求头数据
        if (requestHeader != null && requestHeader.equalsIgnoreCase(jsonRequestHead)) {
            HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
            //获取请求体内部的流数据
            InputStream inputStream = inputMessage.getBody();
            String requestBody = doReadRequestBody(inputStream);
            LogUtils.info(getClass(), "获取请求体内部的参数数据内容是:" + requestBody);
            if (requestBody != null && requestBody.length() > 0) {
                //使用fastJson将请求体数据转换成对象数据
                Object result = FastJsonUtils.jsonToBean(requestBody, requestEntityClass);
                LogUtils.info(getClass(), "转化的对象内容是：" + result);
                return result;
            }
        } else {
            //如果是混合 form表单提交
            if (requestHeader != null && requestHeader.contains(formReqHead)) {
                FileUpload fileUpload = prepareFileUpload();
                RequestContext reqContext = new ServletRequestContext(servletRequest);
                List<FileItem> fileItemList = fileUpload.parseRequest(reqContext);
                Iterator<FileItem> fileItemIt = fileItemList.iterator();
                Map<String, Object> reqParam = new HashMap<>();
                while (fileItemIt.hasNext()) {
                    FileItem fileItem = fileItemIt.next();
                    if (fileItem.isFormField()) {
                        reqParam.put(fileItem.getFieldName(), fileItem.getString());
                    }
                }
                LogUtils.info(getClass(), "获取到数据内容是:" + reqParam);
                return BeanUtils.copyMapToBean(requestEntityClass, reqParam);
            }

            String requestBody = doReadRequestBody(servletRequest.getInputStream());
            if (Utils.isBlank(requestBody)) {
                Map<String, String[]> paramMap = servletRequest.getParameterMap();
                Map<String, Object> transferParam = UrlToMapHelper.converter(paramMap);
                LogUtils.info(getClass(), "POST Map请求转换的map参数数据是:" + transferParam);
                Object targetObj = BeanUtils.copyMapToBean(requestEntityClass, transferParam);
                //返回空的对象
                if (targetObj == null) {
                    return requestEntityClass.newInstance();
                }
                return targetObj;
            }
            LogUtils.info(getClass(), "获取请求体内部的参数数据内容是:" + requestBody);
            //如果请求体内容是json格式数据
            if (!Utils.isBlank(requestBody) && FastJsonUtils.isJson(requestBody)) {
                return FastJsonUtils.jsonToBean(requestBody, requestEntityClass);
            } else {
                //先url encoding进行解码
                requestBody = URLDecoder.decode(requestBody, "utf-8");
                Map<String, Object> reqParam = UrlToMapHelper.converter(requestBody);
                return BeanUtils.copyMapToBean(requestEntityClass, reqParam);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private FileUpload prepareFileUpload() throws IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(3145728);
        return new ServletFileUpload(factory);

    }

    /**
     * 读取请求体内部的数据
     *
     * @param in 请求
     * @return 请求体数据
     */
    private String doReadRequestBody(InputStream in) {
        return UrlToMapHelper.readInputStream(in);
    }

    @SuppressWarnings("unused")
    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

}
