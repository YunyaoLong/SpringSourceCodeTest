package com.springtest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringtestApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringtestApplication.class, args);
		ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("beans.xml");
		System.out.println(context.getBean("person"));
	}

}
