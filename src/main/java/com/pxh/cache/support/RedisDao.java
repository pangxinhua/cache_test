package com.pxh.cache.support;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisDao {
	private RedisTemplate<?, ?> redisTemplate;
	
	public RedisDao(){
	}
	
	public RedisDao(RedisTemplate<?, ?> redisTemplate){
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<?, ?> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void store(final byte [] key, final byte[] val, final long timeout){
		this.redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection connection) throws DataAccessException {
				if(timeout>0){
					connection.setEx(key, timeout, val);
				}else{
					connection.set(key, val);
				}
				return 1;
			}
		});
	}
	
	public byte[] get(final byte [] key){
		return this.redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key);
			}
		});
	}

}
