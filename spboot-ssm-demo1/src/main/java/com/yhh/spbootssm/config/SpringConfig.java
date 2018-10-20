package com.yhh.spbootssm.config;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;


/**
 * 发布到独立的Tomcat需要继承SpringBootServletInitializer类并重写configure方法
 */
//@Configuration // 通过该注解来表明该类是一个Spring的配置，相当于一个xml文件
//@ConfigurationProperties(prefix = "spring.datasource")
public class SpringConfig  {

    /**
     * 手动配置数据源总是出错，只好在application.properties里配置了
     */


    @Value("${driver-class-name}")
    private String driverClassName;
    @Value("${url}")
    private String dburl;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Primary
    @Bean//(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource () {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dburl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        return dataSource;
    }


}
