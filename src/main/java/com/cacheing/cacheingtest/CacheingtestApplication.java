package com.cacheing.cacheingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2; 



@SpringBootApplication
@EnableSwagger2
public class CacheingtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheingtestApplication.class, args);
	}
}