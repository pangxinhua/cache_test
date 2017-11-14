package com.pxh.cache.conf;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.pxh.cache.IKeyGenarator;
import com.pxh.cache.ISerializer;
import com.pxh.cache.support.ProtobufRedisSerializer;

@Configuration
public class CacheConf {

	@Bean
	@ConditionalOnMissingBean(IKeyGenarator.class)
	public IKeyGenarator keyGenerator(@Value("${cache.keyPrefix:pxh:cache}") final String keyPrefix){
		return new IKeyGenarator() {
			
			@Override
			public String getKeyPrefix() {
				return keyPrefix;
			}
			
			@Override
			public String generate(Object target, Method method, Object... params) {
				String keyPrefix = this.getKeyPrefix();
				StringBuilder sb = new StringBuilder();
				if(keyPrefix!=null){
					sb.append(keyPrefix).append(":");
				}
				sb.append(target.getClass().getSimpleName()).append(".").append(method.getName());
				
				if(params!=null){
					int paramLength = params.length;
					if(paramLength>0){
						sb.append("@");
					}
					int lastIndex = params.length - 1;
					
					for (int i = 0; i < paramLength; i++) {
						if(params[i] == null){
							continue;
						}
						
						sb.append(params[i].toString());
						if(i!=lastIndex){
							sb.append("_");
						}
					}
				}
				
				return sb.toString();
			}
		};
	}
	

	
	@SuppressWarnings("rawtypes")
	@Bean
	@ConditionalOnMissingBean(ISerializer.class)
	public ISerializer serializer(RedisTemplate redisTemplate,@Value("${cache.protobuf.wrapedClazzString:}") String wrapedClazzString){
		return new ProtobufRedisSerializer(redisTemplate, wrapedClazzString);
	}

}
