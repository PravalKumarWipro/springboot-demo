package com.caching.cachingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/* The `CachingtestApplication` class serves as the entry point and is responsible for launching the application.
It contains the `main()` method, which initializes the application context and starts the Spring framework */
@SpringBootApplication
@EnableSwagger2
public class CachingtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingtestApplication.class, args);
	}
}