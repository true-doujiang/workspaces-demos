package com.yhh.spbootssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableTransactionManagement//开启事务管理
//@ComponentScan//(basePackages = "com.yhh.spbootssm")
//@PropertySource(value = { "classpath:application.properties" }, ignoreResourceNotFound = true)
@MapperScan(basePackages = "com.yhh.spbootssm.mapper")
@SpringBootApplication
public class SpbootSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbootSsmApplication.class, args);
    }
}
