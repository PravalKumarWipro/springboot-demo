package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/* Interacts with Redis for caching purposes */
@Service
public class RedisClient implements GenericCacheClient {

    @Autowired
    RedissonClient redissonClient;
    @Value("${cache.name:Cache}")
    String cacheName;

    private static final Logger logger= LoggerFactory.getLogger(RedisClient.class);

    /* Retrieves a value from the Redis cache based on the provided key */
    public String getValueById(String key) throws CacheNotFoundException {
        try {
            RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
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
            RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
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
    public void saveOrUpdate(CacheMap cacheMap) {
       try {
           RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
           logger.info("REDIS >>> trying to added user with key :: " + cacheMap.getKey());
           String existingValue = userCache.get(cacheMap.getKey());
           if(existingValue !=null){
               throw new KeyExistsException("key "+cacheMap.getKey()+" already exists, cannot continue!");
           }
           logger.info("REDIS >>> added user with key :: " + cacheMap.getKey());
           userCache.put(String.valueOf(cacheMap.getKey()), cacheMap.getValue(), cacheMap.getTtl(), TimeUnit.SECONDS);
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
