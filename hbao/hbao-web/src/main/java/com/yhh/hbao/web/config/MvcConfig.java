package com.yhh.hbao.web.config;

import com.yhh.hbao.web.conerter.CustomArgumentRequestAnyResolver;
import com.yhh.hbao.core.utils.SpringContextUtils;
import com.yhh.hbao.web.exception.GlobalExceptionHandler;
import com.yhh.hbao.web.interceptor.ModifyIntereptor;
import com.yhh.hbao.web.interceptor.RequestIntereptor;
import com.yhh.hbao.web.model.ResultResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.google.common.collect.Lists;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by yangjj.
 *
 * @DATE 2018/1/8 - 15:47
 * @company WeiMob
 * @description web mvc相关配置
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.yhh")
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new CustomArgumentRequestAnyResolver());
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        fastJsonConfig.setSerializeConfig(serializeConfig);
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(fastJsonHttpMessageConverter);
    }

    @Bean(name = "proxyBean")
    public BeanNameAutoProxyCreator proxyBean() {
        BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
        proxyCreator.setBeanNames("*Controller");
        proxyCreator.setInterceptorNames("validateInterceptor");
        return proxyCreator;
    }

    @Bean
    public FilterRegistrationBean exceptionHandlerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
                try {
                    chain.doFilter(req, resp);
                } catch (Exception e) {
                    HttpServletResponse httpResp = WebUtils.toHttp(resp);
                    httpResp.setContentType("application/json");
                    Throwable targetException = e.getCause() != null ? e.getCause() : e;
                    ResultResponse response = SpringContextUtils.getBean(GlobalExceptionHandler.class).handler(WebUtils.toHttp(req), targetException);
                    httpResp.getWriter().write(JSON.toJSONString(response));
                }
            }

            @Override
            public void destroy() {
            }
        });
        registration.addUrlPatterns("/api");
        registration.setName("exceptionHandlerFilter");
        registration.setOrder(1);
        return registration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getRequestInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(this.getModifyInterceptor()).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }

    @Bean   //把我们的拦截器注入为bean
    public HandlerInterceptor getRequestInterceptor(){
        return new RequestIntereptor();
    }

    @Bean   //把我们的拦截器注入为bean
    public HandlerInterceptor getModifyInterceptor(){
        return new ModifyIntereptor();
    }
}
