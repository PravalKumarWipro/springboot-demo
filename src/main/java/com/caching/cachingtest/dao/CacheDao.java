package com.caching.cachingtest.dao;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import com.caching.cachingtest.model.CacheMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/* DAO class for caching CRUD operations */
@Service
public class CacheDao {

    @Value("${cache.client:APACHE_IGNITE}")
    String cacheClient;
    @Autowired
    private ApacheIgniteClient apacheIgniteClient;

    @Autowired
    private RedisClient redisClient;

    private static final Logger logger = LoggerFactory.getLogger(CacheDao.class);

    /* Retrieves a value from the cache based on the provided key */
    public String getUserById(String key) throws CacheNotFoundException {
        try {
            logger.info("Retrieving value for key: "+key + " from " +cacheClient);
            return getClient().getValueById(key);
        }catch(CacheNotFoundException e){
            logger.error("Cache : " + cacheClient+ "  not found: ");
            throw e;
        }
    }

    /* Deletes a value from the cache using the specified key */
    public Boolean delete(String key) {
       try {
           logger.info(" Deleting key " + key + " from cache: " + cacheClient);
           return getClient().delete(key);
       }catch(CacheNotFoundException e){
           logger.error("Failed to delete key from cache:  "+cacheClient );
           throw e;
       }
    }

    /* Saves or updates a value in the cache with the given key and value */
    public void saveOrUpdate(CacheMap cacheMap) {
        try {
            synchronized (this){
                getClient().saveOrUpdate(cacheMap);
                logger.info("Added key :  " + cacheMap.getKey() + " into cache " + cacheClient);
            }
        }catch (KeyExistsException keyExistsException){
            throw  keyExistsException;
        }catch (UnableToAddKeyException e) {
            logger.error("Unable to add key  " + cacheMap.getKey() + "to cache " + cacheClient);
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
        logger.info("Selected cache client :  " + genericCacheClient);
        return genericCacheClient;
    }
}
