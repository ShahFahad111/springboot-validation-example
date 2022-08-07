package com.javavalidation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo())
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).paths(PathSelectors.any())
				.build();
	}
	
	public ApiInfo getInfo() {
		/*
		 * return new ApiInfo( "My-Project Api", "Api for My Project", "V1",
		 * "NA terms of service url", new Contact("Team Name",
		 * "www.somexyzteamcontact.com", "NA"), "A license given", "NA");
		 */
		return new ApiInfoBuilder().title("API Reference").version("1.0.0")
	            .description("something")
	            .build();
	}
}
