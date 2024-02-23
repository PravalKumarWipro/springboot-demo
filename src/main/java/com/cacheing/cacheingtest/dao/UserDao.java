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


    public String getUserById(int userId) throws UserNotFoundException {
        return getClient().getUserById(userId);
    }

    public Boolean delete(int userId) {
        return getClient().delete(userId);
    }

    public void saveOrUpdate(int userId, String userName) {
        getClient().saveOrUpdate(userId, userName);
    }

    public GenericCacheClient getClient() {
        GenericCacheClient genericCacheClient = apacheIgniteClient;
        if (unleashCustomClient.isRedisEnabled()) {
            genericCacheClient = redisClient;
        }
        return genericCacheClient;
    }
}
