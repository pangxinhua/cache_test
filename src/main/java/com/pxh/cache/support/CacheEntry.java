package com.pxh.cache.support;

/**
 * @Title: CacheEntry.java
 * @Package com.pxh.redisCache.conf
 * @Description: TODO
 * @date 2017年11月1日 下午5:33:10
 * @version V1.0
 */
public class CacheEntry<T> {
	
	private Class<T> clazz;

	private T obj;

	public CacheEntry(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

}