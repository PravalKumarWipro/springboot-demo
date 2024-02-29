package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CacheDao {

    @Value("${cache.client:APACHE_IGNITE}")
    String cacheClient;
    @Autowired
    private ApacheIgniteClient apacheIgniteClient;

    @Autowired
    private RedisClient redisClient;

//    @Autowired
//    private UnleashCustomClient unleashCustomClient;


    public String getUserById(int key) throws CacheNotFoundException {
        return getClient().getValueById(key);
    }

    public Boolean delete(int key) {
        return getClient().delete(key);
    }

    public void saveOrUpdate(int key, String value) {
        getClient().saveOrUpdate(key, value);
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
