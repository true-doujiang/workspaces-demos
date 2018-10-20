package com.yhh.spbootssm.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
//@AutoConfigureAfter(value = MybatisConfig.class)
public class MapperScannerConfig {


//    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer () {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.yhh.spbootssm.mapper");
        System.out.println(Thread.currentThread().getName() + " MapperScannerConfigurer = " + mapperScannerConfigurer);
        return mapperScannerConfigurer;
    }
}
