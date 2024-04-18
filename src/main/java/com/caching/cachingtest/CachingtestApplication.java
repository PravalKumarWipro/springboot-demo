package com.caching.cachingtest;

import com.caching.cachingtest.controller.CacheController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

/* The `CachingtestApplication` class serves as the entry point and is responsible for launching the application */
@SpringBootApplication
@EnableSwagger2
public class CachingtestApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);


	@Value("${cache.name:Cache}")
	String cacheName;

	@Value("${cache.client:APACHE_IGNITE}")
	String cacheClient;

	public static void main(String[] args)
	{
		SpringApplication app = new SpringApplication(CachingtestApplication.class);
		app.setLazyInitialization(true);
		app.run(args);
	}


	@PostConstruct
	public void configure(){
		LOGGER.info("Cache Name Configured :: {}", cacheName);
		LOGGER.info("Cache Client Configured :: {}", cacheClient);
	}
}