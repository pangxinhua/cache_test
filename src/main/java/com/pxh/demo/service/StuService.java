package com.pxh.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pxh.cache.annotation.Cacheable;
import com.pxh.demo.entity.Stu;

@Service
public class StuService {

	public Stu getByAge(int age) {
		return new Stu(age, "张三" + age);
	}

	@Cacheable
	public List<Stu> list() {
		ArrayList<Stu> stus = new ArrayList<>();
		stus.add(new Stu(1, "张三"));
		stus.add(new Stu(2, "李四"));
		return stus;
	}

	@Cacheable
	public Map<String, Stu> map() {
		Map<String, Stu> stus = new HashMap<>();
		stus.put("1", new Stu(1, "张三"));
		stus.put("2", new Stu(2, "李四"));
		stus.put("4", new Stu(2, "王五"));
		return stus;
	}
	
	@Cacheable(timeout=10)
	public Integer age() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}
	@Cacheable
	public Integer age1() {
		return 2;
	}
	public String info() {
		return "testinfo";
	}
}
