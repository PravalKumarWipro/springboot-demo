package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    @Value("${cache.client:APACHE_IGNITE}")
    String cacheClient;

    @Autowired
    ApacheIgniteClient apacheIgniteClient;

    @Autowired
    RedisClient redisClient;


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
        switch (cacheClient) {
            case AppConstants.CACHE_REDIS: {
                genericCacheClient = redisClient;
                break;
            }
        }
        return genericCacheClient;
    }
}
