package com.yhh.spbootssm.config;


import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
public class MybatisConfig {

//    @Bean
//    @ConditionalOnMissingBean
    public SqlSessionFactoryBean sqlSessionFactoryBean (DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 设置mybatis的主配置文件
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource mybatisConfigXml = resolver.getResource("classpath:mybatis/sqlMapConfig.xml");

        factoryBean.setConfigLocation(mybatisConfigXml);
        // 设置别名包
        factoryBean.setTypeAliasesPackage("com.yhh.spbootssm.domain");

        System.out.println(Thread.currentThread().getName() + " DataSource = " + dataSource);

        return factoryBean;
    }


}
