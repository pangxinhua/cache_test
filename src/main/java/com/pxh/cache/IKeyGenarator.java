package com.pxh.cache;

import java.lang.reflect.Method;

public interface IKeyGenarator {
	
	public String getKeyPrefix();
	
	public String generate(Object target, Method method, Object... params);
}
