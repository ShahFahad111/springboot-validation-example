package com.javavalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
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
