package com.yhh.hbao.web.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * redis 主机信息
     */
    @Value("${spring.redis.host}")
    private String redisHost;

    /**
     * redis 端口号
     */
    @Value("${spring.redis.port}")
    private Integer port;

    /**
     * redis password
     */
    @Value("${spring.redis.password}")
    private String redisPass;
    /**
     * redis 超时时间
     */
    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;
    @Bean
    public ShardedJedisPool redisPoolFactory() {
        JedisPoolConfig config =new JedisPoolConfig();//Jedis池配置
        config.setMaxIdle(maxIdle);//最大活动的对象个数
        config.setMaxWaitMillis(timeout);//对象最大空闲时间
        config.setMinIdle(minIdle);//获取对象时最大等待时间
        config.setTestOnBorrow(true);
        List jdsInfoList =new ArrayList();
        JedisShardInfo infoA = new JedisShardInfo(redisHost, port);
        if(!StringUtils.isEmpty(redisPass)){
            infoA.setPassword(redisPass);
        }
        jdsInfoList.add(infoA);
        return new ShardedJedisPool(config, jdsInfoList);
    }


}
