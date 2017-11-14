package com.pxh.cache;

public interface ISerializer {

	public <T> T deserialize(byte[] key, Class<T> cls);

	public <T> void serialize(byte[] key, T obj, long timeout);

}
