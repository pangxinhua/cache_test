package com.pxh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.pxh.demo.service.StuService;

@SpringBootApplication
public class AppTest {
	private static Logger log = LoggerFactory.getLogger(AppTest.class);
	

	public static void main(String[] args) throws Exception {
		// start
		ConfigurableApplicationContext context = SpringApplication.run(AppTest.class, args);
		final StuService service = context.getBean(StuService.class);
//		Stu stu = service.getByAge(1);
//		stu = service.getByAge(1);
//		log.info(Thread.currentThread().getId()+" : "+stu);
		
	Thread th1= new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.age());
				log.info(Thread.currentThread().getId()+" : "+service.age());
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.age1());
				log.info(Thread.currentThread().getId()+" : "+service.age1());
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.info());
				log.info(Thread.currentThread().getId()+" : "+service.info());
				
			}
		});
		
	Thread th2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.age1());
				log.info(Thread.currentThread().getId()+" : "+service.age1());
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.age());
				log.info(Thread.currentThread().getId()+" : "+service.age());
				log.info(Thread.currentThread().getId()+" : "+"###########################");
				log.info(Thread.currentThread().getId()+" : "+service.info());
				log.info(Thread.currentThread().getId()+" : "+service.info());
				
			}
		});
		
		th1.start();
		th2.start();
		th1.join();
		th2.join();
	}

}
