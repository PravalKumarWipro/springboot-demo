package com.caching.cachingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/* The `CachingtestApplication` class serves as the entry point and is responsible for launching the application */
@SpringBootApplication
@EnableSwagger2
public class CachingtestApplication {
	public static void main(String[] args)
	{
		SpringApplication app = new SpringApplication(CachingtestApplication.class);
		app.setLazyInitialization(true);
		app.run(args);
	}
}