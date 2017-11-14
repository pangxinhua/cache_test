package com.pxh.cache.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.RedisTemplate;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.pxh.cache.ISerializer;

public class ProtobufRedisSerializer implements ISerializer {
	private Map<Class<?>, Schema<?>> cachedSchemas = new ConcurrentHashMap<>();
	private Set<Class<?>> wrapedClazzes = new HashSet<>();
	private RedisDao redisDao;
	private String wrapedClazzString;

	private void init() {
		System.getProperties().setProperty("protostuff.runtime.always_use_sun_reflection_factory", "true");
		
		this.wrapedClazzes.add(Collection.class);
		this.wrapedClazzes.add(Map.class);
		if (isNotEmpty(this.wrapedClazzString)) {
			String[] arr = wrapedClazzString.split(",");
			try {
				for (String s : arr) {
					if (isNotEmpty(s)) {
						wrapedClazzes.add(Class.forName(s));
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public ProtobufRedisSerializer(RedisTemplate<?, ?> redisTemplate) {
		this(redisTemplate, null);
	}

	public ProtobufRedisSerializer(RedisTemplate<?, ?> redisTemplate, String wrapedClazzString) {
		this.redisDao = new RedisDao(redisTemplate);
		this.wrapedClazzString = wrapedClazzString;
		this.init();
	}

	@Override
	public <T> T deserialize(byte[] key, Class<T> cls) {
		byte[] data = this.redisDao.get(key);
		if (data == null) {
			return null;
		}
		return (T) this.deserializeObj(data, cls);
	}

	@Override
	public <T> void serialize(byte[] key, T obj, long timeout) {
		byte[] val = serializeObj(obj);
		this.redisDao.store(key, val, timeout);
	}

	@SuppressWarnings({ "unchecked" })
	private <T> Schema<T> getSchema(Class<T> cls) {
		Class<?> clazz = cls;
		if (needWrapedClass(cls)) {
			clazz = CacheEntry.class;
		}
		Schema<T> schema = (Schema<T>) cachedSchemas.get(clazz);
		if (schema == null) {
			schema = (Schema<T>) RuntimeSchema.createFrom(clazz);
			if (schema != null) {
				cachedSchemas.put(clazz, schema);
			}
		}
		return schema;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T deserializeObj(byte[] data, Class<T> cls) {
		try {
			Schema<T> schema = getSchema(cls);
			T message = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(data, message, schema);
			if (needWrapedClass(cls)) {
				return (T) ((CacheEntry) message).getObj();
			}
			return message;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	private <T> byte[] serializeObj(T obj) {
		Class<T> cls = (Class<T>) obj.getClass();
		if (needWrapedClass(obj.getClass())) {
			CacheEntry<T> entry = (CacheEntry<T>) new CacheEntry<>(obj.getClass());
			entry.setObj(obj);
			obj = (T) entry;
			cls = (Class<T>) CacheEntry.class;
		}

		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema<T> schema = getSchema(cls);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}

	}

	private boolean needWrapedClass(Class<?> targetClazz) {
		for (Iterator<Class<?>> iter = this.wrapedClazzes.iterator(); iter.hasNext();) {
			if (iter.next().isAssignableFrom(targetClazz)) {
				return true;
			}
		}
		return false;
	}

	private boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	private boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
}
