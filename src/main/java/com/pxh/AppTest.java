package com.pxh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.pxh.demo.service.StuService;

@SpringBootApplication
public class AppTest {

	public static void main(String[] args) throws Exception {
		// start
		ConfigurableApplicationContext context = SpringApplication.run(AppTest.class, args);
		StuService service = context.getBean(StuService.class);
//		Stu stu = service.getByAge(1);
//		System.out.println(stu);
//		stu = service.getByAge(1);
//		System.out.println(stu);
		
		System.out.println("###########################");
		System.out.println(service.list());
		System.out.println(service.list());
		
		System.out.println("###########################");
		
		System.out.println(service.map().values());
		System.out.println(service.map().values());
		System.out.println("###########################");
		System.out.println(service.age());
		System.out.println(service.age());
		System.out.println("###########################");
		System.out.println(service.age1());
		System.out.println(service.age1());
		System.out.println("###########################");
		System.out.println(service.info());
		System.out.println(service.info());
	}

}
