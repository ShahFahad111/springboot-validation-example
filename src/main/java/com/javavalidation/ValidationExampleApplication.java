package com.javavalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ValidationExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidationExampleApplication.class, args);
		System.out.println("************************************************************");
		System.out.println("**************SPRING JAVA VALIDATION API********************");
		System.out.println("************************************************************");
		
		/*
		 * log.info("************************************************************");
		 * log.info("**************Spring Java Validation API********************");
		 * log.info("************************************************************");
		 */
	}

}
