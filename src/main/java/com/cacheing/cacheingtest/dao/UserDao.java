package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class UserDao {

    @Autowired
    private ApacheIgniteClient apacheIgniteClient;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UnleashCustomClient unleashCustomClient;


    public String getUserById(int userId,String token) throws UserNotFoundException {
        return getClient(token).getUserById(userId);
    }

    public Boolean delete(int userId,String token) {
        return getClient(token).delete(userId);
    }

    public void saveOrUpdate(int userId, String userName,String token) {
        getClient(token).saveOrUpdate(userId, userName);
    }

    public GenericCacheClient getClient(String token) {
        GenericCacheClient genericCacheClient = apacheIgniteClient;
        if (unleashCustomClient.isRedisEnabled(token)) {
            genericCacheClient = redisClient;
        }
        return genericCacheClient;
    }
}
