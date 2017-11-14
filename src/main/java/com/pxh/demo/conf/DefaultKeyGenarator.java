package com.pxh.demo.conf;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;

import com.pxh.cache.IKeyGenarator;

//@Component
public class DefaultKeyGenarator implements IKeyGenarator{
	
	@Value("${cache.keyPrefix:pxh:cachess}")
	private String keyPrefix;

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

	@Override
	public String getKeyPrefix() {
		return keyPrefix;
	}

}
