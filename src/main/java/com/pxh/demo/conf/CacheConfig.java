package com.pxh.demo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.pxh.cache.ISerializer;
import com.pxh.cache.support.FastJsonRedisSerializer;

@Configuration
public class CacheConfig {

	@SuppressWarnings("rawtypes")
	@Bean
	public ISerializer serializer(RedisTemplate redisTemplate){
		return new FastJsonRedisSerializer(redisTemplate);
	}
}
