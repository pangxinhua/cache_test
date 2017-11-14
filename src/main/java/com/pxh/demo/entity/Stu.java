package com.pxh.demo.entity;

import java.io.Serializable;

public class Stu implements Serializable {

	private static final long serialVersionUID = -4872691964109751910L;

	private int age;
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stu(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	public Stu() {
		super();

	}

	@Override
	public String toString() {
		return "Stu [age=" + age + ", name=" + name + "]";
	}
	
}
