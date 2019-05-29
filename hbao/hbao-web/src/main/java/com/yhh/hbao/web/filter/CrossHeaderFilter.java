package com.yhh.hbao.web.filter;

import com.yhh.hbao.core.enums.HeaderEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yhh
 * @date 1/13/17 5:09 PM
 */
@Order(2)
@Component
public class CrossHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // CORS "pre-flight" request
        response.addHeader(HeaderEnum.AccessControlAllowOrigin.value, "*");
        response.addHeader(HeaderEnum.AccessControlAllowMethods.value, "GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader(HeaderEnum.AccessControlAllowHeaders.value, "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,Origin,Accept");
        response.addHeader(HeaderEnum.AccessControlMaxAge.value, "3600");//1H
        response.addHeader(HeaderEnum.AccessControlAllowCredentials.value, "true");
        filterChain.doFilter(request, response);
    }
}
