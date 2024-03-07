package com.caching.cachingtest.dao;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/* Serves as an  intermediary between application and cache.It's responsible for all cache-related operations */
@Service
public class CacheDao {

    @Value("${cache.client:APACHE_IGNITE}")
    String cacheClient;
    @Autowired
    private ApacheIgniteClient apacheIgniteClient;

    @Autowired
    private RedisClient redisClient;

    private static final Logger logger= LoggerFactory.getLogger(CacheDao.class);

    /* Retrieves a value from the cache based on the provided key */
    public String getUserById(int key) throws CacheNotFoundException {
        try {
            logger.info("Retrieving value for key: "+key);
            return getClient().getValueById(key);
        }catch(CacheNotFoundException e){
            logger.error("Cache not found: " + e.getMessage());
            throw e;
        }
    }

    /* Deletes a value from the cache using the specified key */
    public Boolean delete(int key) {
       try {
           logger.info(" Deleting key " + key+ " from cache");
           return getClient().delete(key);
       }catch(CacheNotFoundException e){
           logger.error("Failed to delete key from cache with exception : " +e.getMessage());
           throw e;
       }
    }

    /* Saves or updates a value in the cache with the given key and value */
    public void saveOrUpdate(int key, String value) {
        try{
            getClient().saveOrUpdate(key, value);
            logger.info("Added key :  "+ key);
        }catch (UnableToAddKeyException e){
            logger.error("Unable to add key  " +key);
            throw e;
        }
    }

    /* Determines the appropriate cache client based on the configured type */
        public GenericCacheClient getClient() {
        GenericCacheClient genericCacheClient = apacheIgniteClient;
        switch (cacheClient) {
            case AppConstants.CACHE_REDIS: {
                genericCacheClient = redisClient;
                break;
            }
        }
        logger.info("Selected cache client :  " + genericCacheClient );
        return genericCacheClient;
    }
}
