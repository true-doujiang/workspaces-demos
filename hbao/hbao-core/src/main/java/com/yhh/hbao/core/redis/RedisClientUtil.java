package com.yhh.hbao.core.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * <li>redis工具类</li>
 */
@Component
public class RedisClientUtil {

	private final static Logger LOGGER = Logger.getLogger(RedisClientUtil.class);
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	/**
	 * @param key
	 *            存储key
	 * @param value
	 *            存储数据
	 * @param seconds
	 *            有效时间 单位秒
	 */
	public void set(String key, String value, int seconds) {


		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			if (-1 == seconds) {
				client.set(key, value);
			} else {
				client.setex(key, seconds, value);
			}
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}

	}

	/**
	 * @param key
	 *            存储数据key
	 * @param value
	 *            存储数据
	 */
	public void set(String key, String value) {
		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			client.set(key, value);
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}
	}

	/**
	 * @param key
	 *            存储数据key
	 * @param value
	 *            存储数据
	 */
	public void set(String key, String value, String nxxx, String expx, Integer time) {
		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			client.set(key, value,nxxx,expx,time);
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}
	}

	/**
	 * 根据存储key获取相应的存储value数据
	 * 
	 * @param key
	 *            存储数据key
	 * @return
	 */
	public String get(String key) {
		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			return client.get(key);
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}
		return null;
	}
	/**
	 * 根据存储key是否存在
	 *
	 * @param key
	 *            存储数据key
	 * @return
	 */
	public Boolean exists(String key) {
		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			return client.exists(key);
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}
		return false;
	}

	/**
	 * 根据存储key删除相应的数据
	 * 
	 * @param key
	 *            存储数据key
	 */
	public void del(String key) {
		ShardedJedis client = null;
		try {
			client = shardedJedisPool.getResource();
			client.del(key);
		} catch (JedisConnectionException jedisE) {
			LOGGER.error("redis connectionException exception={}",jedisE);
		} catch (Exception e) {
			LOGGER.error("redis set data error={}",e);
		} finally {
			/** 业务执行完毕，将连接返回给连接池 */
			if (null != client) {
				client.close();
			}
		}
	}
}
