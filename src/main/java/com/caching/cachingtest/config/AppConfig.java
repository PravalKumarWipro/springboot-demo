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
import java.util.Arrays;

/* This class contains application configuration settings related to different caching systems */
@Configuration
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    // Config variable for Apache Ignite
    @Value("${ignite.url}")
    String apacheIgniteUrl;

    // Config variable for Redis
    @Value("${redis.url}")
    String redisBaseUrl;




    /***
     * Method to create and start Apache Ignite Caching Client
     * @return
     */
    @Bean(name = "CustomIgniteClient")
    public IgniteClient igniteClient() {
        IgniteClient client = null;
        try {
            LOGGER.info("Ignite Client creation started");
            ClientConfiguration cfg = new ClientConfiguration().setAddresses(apacheIgniteUrl);
            client = Ignition.startClient(cfg);
            LOGGER.info("Ignite Client created");
        } catch (Exception e) {
            LOGGER.error("Error creating Ignite Client: {}\t stacktrace : {}",e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        return client;
    }


    /***
     * Method to create and start Redis Caching Client
     * @return
     */
    @Bean(name = "CustomRedissonClient")
    public RedissonClient redisClient() {
        RedissonClient client = null;
        try {
            LOGGER.info("Redis Client creation started");
            Config config = new Config();
            config.useSingleServer()
                    .setAddress(redisBaseUrl);
             client = Redisson.create(config);
            LOGGER.info("Redis Client created");
            return client;
        } catch (Exception e) {
            LOGGER.error("Error creating Redis Client: {}\t stacktrace : {}",e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        return client;
    }
}
