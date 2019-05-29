package com.yhh.hbao.web.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring boot 营销中心活动服务
 *
 * @author yhh
 * @E-Mail dingl@163.com
 * @create 2018-05-18 上午10:27
 **/
@SpringBootApplication
@MapperScan("com.yhh.hbao.orm.mapper")
@Configuration
@ComponentScan("com.yhh.hbao")
@EnableScheduling
public class HbaoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HbaoApplication.class);
    }

}