package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.KeyNotExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/*This class interacts with Apache Ignite Cache and performs all cache operations like save/Update, delete and getValueByKey*/
@Component
@Lazy
public class ApacheIgniteClient implements GenericCacheClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheIgniteClient.class);

    @Autowired(required = false)
    IgniteClient igniteClient;

    @Value("${cache.name:Cache}")
    String cacheName;

    @Value("${cache.rebalance.mode:SYNC}")
    String cacheRebalanceMode;

    /***
     * Retrieves the value associated with the given key from the cache
     * @param key
     * @return
     */
    public String getValueById(String key) {
        try {
            LOGGER.info("In getValueById() client ::: APACHE IGNITE searching with key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            String value = clientCache.get(key);
            LOGGER.info("In getValueById() client ::: APACHE IGNITE key :: {} found", key);
            LOGGER.debug("In getValueById() client ::: APACHE IGNITE key :: {} found, value ::{}", key,value);
            return value;
        } catch (Exception e) {
            LOGGER.error("In getValueById() client ::: APACHE IGNITE Error while searching with key :: {}",key);
            return null;
        }
    }


    /***
     * Deletes the entry associated with the given key from the cache
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        try {
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            Boolean status = clientCache.remove(key);
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {} success", key);
            return status;
        } catch (Exception e) {
            LOGGER.error("In delete() client ::: APACHE IGNITE Error while deleting key : {}  from ApacheClient exception : {}, stacktrace : {} ",key, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }


    /***
     * Saves or updates the value associated with the given key in the cache
     * @param cacheMap
     */
    public void save(CacheMap cacheMap) {
        ClientCacheConfiguration cacheConfiguration = new ClientCacheConfiguration();
        cacheConfiguration.setName(cacheName);
        CacheRebalanceMode cacheRebalanceMode = getCacheRebalanceingModeFromConfig();
        LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" trying adding the key : "+ cacheMap.getKey());
        cacheConfiguration.setRebalanceMode(cacheRebalanceMode);
        ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheConfiguration).withExpirePolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, cacheMap.getTtl())));
        clientCache.getConfiguration();
        String existingValue = clientCache.get(cacheMap.getKey());
        if(existingValue !=null){
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed as Key Already exists");
            throw new KeyExistsException("key "+cacheMap.getKey()+" already exists, cannot continue!");
        }
        try {
            clientCache.put(cacheMap.getKey(), cacheMap.getValue());
            LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" success");
            LOGGER.debug("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" success, value ::"+cacheMap.getValue());
        } catch (Exception e) {
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed Error : "+e.getMessage()+"\t stacktrace : "+ Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Error :  " + e.getMessage(), e);
        }
    }

    /***
     *  updates the value associated with the given key in the cache
     * @param cacheMap
     */
    public void update(CacheMap cacheMap) throws KeyNotExistsException{
        ClientCacheConfiguration cacheConfiguration = new ClientCacheConfiguration();
        cacheConfiguration.setName(cacheName);
        CacheRebalanceMode cacheRebalanceMode = getCacheRebalanceingModeFromConfig();
        LOGGER.info("In update() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" trying update the key : "+ cacheMap.getKey());
        cacheConfiguration.setRebalanceMode(cacheRebalanceMode);
        ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheConfiguration).withExpirePolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, cacheMap.getTtl())));
        clientCache.getConfiguration();
        String existingValue = clientCache.get(cacheMap.getKey());
        if(existingValue == null){
            LOGGER.error("In update() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" update the key : "+ cacheMap.getKey()+" failed as Key Not exists");
            throw new KeyNotExistsException("key "+cacheMap.getKey()+" not exists, cannot continue!");
        }
        try {
            clientCache.remove(cacheMap.getKey());
            clientCache.put(cacheMap.getKey(), cacheMap.getValue());
            LOGGER.info("In update() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" updating the key : "+ cacheMap.getKey()+" success");
            LOGGER.debug("In update() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" updating the key : "+ cacheMap.getKey()+" success, value ::"+cacheMap.getValue());
        } catch (Exception e) {
            LOGGER.error("In update() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" updating the key : "+ cacheMap.getKey()+" failed Error : "+e.getMessage()+"\t stacktrace : "+ Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Error :  " + e.getMessage(), e);
        }
    }

    /***
     *
     * @return
     */
    @Override
    public String toString() {
        return "ApacheIgniteClient";
    }

    /***
     * This method returns the using CacheRebalanceMode which is read from config
     * @return
     */

    public CacheRebalanceMode getCacheRebalanceingModeFromConfig(){
        CacheRebalanceMode finalCacheRebalanceMode = null;
        switch(cacheRebalanceMode){
            case "SYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
            case "ASYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.ASYNC;
                break;
            case "NONE":
                finalCacheRebalanceMode =  CacheRebalanceMode.NONE;
                break;
            default:
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
        }
        return finalCacheRebalanceMode;
    }
}
