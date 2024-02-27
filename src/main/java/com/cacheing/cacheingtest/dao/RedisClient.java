package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RedisClient implements GenericCacheClient {

    @Autowired
    RedissonClient redissonClient;

    String CACHE_NAME = "Users";


    public String getValueById(int key) throws CacheNotFoundException {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        String value = userCache.get(String.valueOf(key));
        System.out.println("REDIS >>> searching user with key :: " + key + ", response received from cache :: " + value);
        return value;
    }

    public Boolean delete(int key) {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        String respose = userCache.remove(String.valueOf(key));
        System.out.println("REDIS >>> response after deletion :: " + respose);
        if (respose == null) {
            return false;
        }
        return true;
    }

    public void saveOrUpdate(int key, String value) {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        System.out.println("REDIS >>> added user with key :: " + key);
        userCache.put(String.valueOf(key), value);
    }

    @Override
    public String toString() {
        return "RedisClient";
    }
}
