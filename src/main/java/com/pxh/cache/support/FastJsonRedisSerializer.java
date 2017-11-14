package com.pxh.cache.support;

import java.nio.charset.Charset;

import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pxh.cache.ISerializer;

public class FastJsonRedisSerializer implements ISerializer {

	private RedisDao redisDao;

	private Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private void init() {
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}
	
	public FastJsonRedisSerializer(RedisTemplate<?, ?> redisTemplate){
		this.redisDao = new RedisDao(redisTemplate);
		this.init();
	}

	@Override
	public <T> T deserialize(byte[] key, Class<T> cls) {
		byte[] data = this.redisDao.get(key);
		if(data==null){
			return null;
		}
		String str = new String(data, DEFAULT_CHARSET);
		return JSON.parseObject(str, cls);
	}

	@Override
	public <T> void serialize(byte[] key, T obj, long timeout) {
		byte[] data = JSON.toJSONString(obj, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
		this.redisDao.store(key, data, timeout);
	}

}
