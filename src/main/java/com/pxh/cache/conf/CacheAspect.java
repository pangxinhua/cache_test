package com.pxh.cache.conf;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pxh.cache.IKeyGenarator;
import com.pxh.cache.ISerializer;
import com.pxh.cache.annotation.Cacheable;

@Aspect
@Component
public class CacheAspect {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISerializer serializer;
	
	@Autowired
	private IKeyGenarator keyGenarator;
	
	
	@Pointcut(value = "@annotation(com.pxh.cache.annotation.Cacheable)")
	private void pointCut() {
	}

	@Around(value = "pointCut()")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = getFromCache(pjp);
		if(ret != null){
			return ret;
		}
		
    	return proccedAndCached(pjp);
	}
	
	@AfterThrowing(pointcut = "pointCut()", throwing = "e")
	public void doException(JoinPoint jp, Exception e) {
		if (e != null) {
			String staticPart = jp.getStaticPart().toString();
			String exception = e.toString();
			//this can be replace with notice action 
			logger.error("===============> Exception. staticPart:{}", staticPart, exception);
		}
	}
	
	private Object proccedAndCached(ProceedingJoinPoint pjp) throws Throwable {
		Cacheable cacheable = getMethodSignature(pjp).getMethod().getAnnotation(Cacheable.class);
		Object ret = pjp.proceed();
		if(!cacheable.cacheNullResult() && ret==null){
			return ret;
		}
		
		final long expireTime = cacheable.timeout();
		final String key = genarateKey(pjp);
		this.serializer.serialize(key.getBytes(), ret, expireTime);
		
		return ret;
		
	}

	private String genarateKey(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = getMethodSignature(pjp);
		return keyGenarator.generate(pjp.getTarget(), methodSignature.getMethod(), pjp.getArgs());
	}

	private MethodSignature getMethodSignature(ProceedingJoinPoint pjp) {
		return ((MethodSignature) pjp.getSignature());
	}

	@SuppressWarnings("unchecked")
	private Object getFromCache(ProceedingJoinPoint pjp) {
		final String key = genarateKey(pjp);
		MethodSignature methodSignature = getMethodSignature(pjp);
		return this.serializer.deserialize(key.getBytes(), methodSignature.getReturnType());
	}
	
}
