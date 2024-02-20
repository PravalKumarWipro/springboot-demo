package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.exception.UserNotFoundException;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RedisClient implements GenericCacheClient{

    @Autowired
    RedissonClient redissonClient;

    String CACHE_NAME = "Users";


    public String getUserById(int userId) throws UserNotFoundException {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        String userName = userCache.get(String.valueOf(userId));
        System.out.println("REDIS >>> searching user with userId :: " + userId + ", response received from cache :: " + userName);
        return userName;
    }

    public Boolean delete(int userId) {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        String respose = userCache.remove(String.valueOf(userId));
        System.out.println("REDIS >>> response after deletion :: " + respose);
        if (respose == null) {
            return false;
        }
        return true;
    }

    public void saveOrUpdate(int userId, String userName) {
        RMapCache<String, String> userCache = redissonClient.getMapCache(CACHE_NAME);
        System.out.println("REDIS >>> added user with userId :: " + userId);
        userCache.put(String.valueOf(userId), userName, 60, TimeUnit.SECONDS);
    }
}
