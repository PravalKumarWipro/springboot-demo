package com.caching.igniteexternaldb;

import com.caching.igniteexternaldb.controller.CacheController;
import com.caching.igniteexternaldb.model.Person;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

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
//		app.setLazyInitialization(true);
		app.run(args);
	}



	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			LOGGER.info("***********************Application Started ***************************");
			LOGGER.info("Cache Name Configured :: {}", cacheName);
			LOGGER.info("Cache Client Configured :: {}", cacheClient);
		};
	}
}