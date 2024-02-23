package com.cacheing.cacheingtest.config;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class AppConfig {


    @Value("${ignite.url}")
    String apacheIgniteUrl;

    @Value("${redis.url}")
    String redisBaseUrl;

    @Value("${cacheDuration:300}")
    public long cacheDuration;

    String CACHE_NAME = "Users";

    @Bean
    public IgniteClient igniteClient() {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses(apacheIgniteUrl);
        IgniteClient client = Ignition.startClient(cfg);
        return client;
    }


    @Bean(name = "RedissonClient")
    public RedissonClient redisClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisBaseUrl);
        RedissonClient client = Redisson.create(config);
        return client;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(3)).build();
    }
}
