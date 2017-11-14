package com.pxh.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cacheable {
	
	/**
	 * Set the specified expire time, in seconds
	 * @return
	 */
	long timeout() default -2; 
	
	/**
	 * cache
	 * @return
	 */
	boolean cacheNullResult() default false;
}
