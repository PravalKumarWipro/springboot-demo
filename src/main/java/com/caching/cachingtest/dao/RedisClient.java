package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* Interacts with Redis for caching purposes */
@Service
public class RedisClient implements GenericCacheClient {

    @Autowired
    RedissonClient redissonClient;

    String CACHE_NAME = "Users";
    private static final Logger logger= LoggerFactory.getLogger(RedisClient.class);

    /* Retrieves a value from the Redis cache based on the provided key */
    public String getValueById(String key) throws CacheNotFoundException {
        try {
            RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
            String value = userCache.get(String.valueOf(key));
            logger.info("REDIS >>> searching user with key :: " + key + ", response received from cache :: " + value);
            return value;
        }catch(CacheNotFoundException e){
            logger.error("User not found in cache for key: " + key);
            throw e;
        }
    }

    /* Deletes a value from the Redis cache based on the provided key */
    public Boolean delete(String key) {
        try {
            RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
            String respose = userCache.remove(String.valueOf(key));
            logger.info("REDIS >>> response after deletion :: " + respose);
            if (respose == null) {
                return false;
            }
            return true;
        }catch(CacheNotFoundException e){
            logger.error("Error while deleting a key : "+e.getMessage());
            throw e;
        }
    }

    /* Saves Or Updates key-value to the Redis cache  */
    public void saveOrUpdate(String key, String value) {
       try {
           RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
           logger.info("REDIS >>> added user with key :: " + key);
           userCache.put(String.valueOf(key), value);
       }catch(Exception e){
           logger.error("Error while saving or updating cache: " + e.getMessage());
           throw e;
       }
    }

    @Override
    public String toString() {
        return "RedisClient";
    }
}
