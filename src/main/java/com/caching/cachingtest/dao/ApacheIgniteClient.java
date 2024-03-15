package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

/*This class interacts with Apache Ignite Cache and performs all cache operations like save/Update, delete and getValueByKey*/
@Component
public class ApacheIgniteClient implements GenericCacheClient {
    private static final Logger logger = LoggerFactory.getLogger(ApacheIgniteClient.class);

    @Autowired
    IgniteClient igniteClient;
    @Value("${cache.name:Cache}")
    String cacheName;


   /*  Retrieves the value associated with the given key from the cache  */
    public String getValueById(String key) {
        try {
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            String value = clientCache.get(key);
            logger.info("APACHE IGNITE >>> searching user with key :: " + key + ", response received from cache :: " + value);
            return value;
        } catch (Exception e) {
            logger.error("Error while searching user with key from Apache Ignite");
            return null;
        }
    }

    /* Deletes the entry associated with the given key from the cache */
    public Boolean delete(String key) {
        try {
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            Boolean status = clientCache.remove(key);
            logger.info("APACHE IGNITE >>> response after deletion :: " + status);
            return status;
        } catch (Exception e) {
            logger.error("Error while deleting key  " + key + "  from ApacheClient ");
            return false;
        }
    }

    /* Saves or updates the value associated with the given key in the cache */
    public void saveOrUpdate(CacheMap cacheMap) {
        ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName).withExpirePolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, cacheMap.getTtl())));
        logger.info("APACHE IGNITE >>>trying to added user with key :: " + cacheMap.getKey());
        String existingValue = clientCache.get(cacheMap.getKey());
        if(existingValue !=null){
            throw new KeyExistsException("Key : "+cacheMap.getKey()+" Exists");
        }
        try {
            logger.info("APACHE IGNITE >>> added user with key :: " + cacheMap.getKey());
            clientCache.put(cacheMap.getKey(), cacheMap.getValue());
        } catch (Exception e) {
            logger.error("Error while saving/updating the value for key");
            throw new RuntimeException("Error :  " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "ApacheIgniteClient";
    }

}
