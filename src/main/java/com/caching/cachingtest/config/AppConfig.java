package com.caching.cachingtest.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/* This class contains application configuration settings related to different caching systems */
@Configuration
public class AppConfig {
    private static final Logger logger= LoggerFactory.getLogger(AppConfig.class);

    // Config variable for Apache Ignite
    @Value("${ignite.url}")
    String apacheIgniteUrl;

    // Config variable for Redis
    @Value("${redis.url}")
    String redisBaseUrl;



    /* Method to create and start Apache Ignite Caching Client */
    @Bean
    public IgniteClient igniteClient() {
        try {
            ClientConfiguration cfg = new ClientConfiguration().setAddresses(apacheIgniteUrl);
            IgniteClient client = Ignition.startClient(cfg);
            logger.info("Ignite Client created");
            return client;
        } catch (Exception e) {
            logger.error("Error creating Ignite Client: ");
            throw new RuntimeException("Error creating Ignite Client : " + e.getMessage(), e);
        }
    }

    /* Method to create and start Redis Caching Client */
    @Bean(name = "RedissonClient")
    public RedissonClient redisClient() {
        try {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress(redisBaseUrl);
            RedissonClient client = Redisson.create(config);
            logger.info("Redis Client created");
            return client;
        } catch (Exception e) {
            logger.error("Error creating Redis Client: ");
            throw new RuntimeException("Error creating Redis Client : " + e.getMessage(), e);
        }
    }

    /* RestTemplate builder with ConnectTimeOUT and ReadTimeOut settings*/
    @Bean
    public RestTemplate restTemplate() {
        try {
            return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(3)).build();
        } catch (Exception e) {
            logger.error("Error creating RestTemplate: ");
            throw new RuntimeException("Error creating RestTemplate: " + e.getMessage(), e);
        }
    }
}
