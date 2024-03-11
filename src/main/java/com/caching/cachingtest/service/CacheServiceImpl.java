package com.caching.cachingtest.service;

import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* This class provides essential methods for managing cache data*/
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    public CacheDao cacheDao;
    private static final Logger logger= LoggerFactory.getLogger(CacheServiceImpl.class);

    /*  Retrieves a value from the cache based on the given key */
    @Override
    public String getValueByKey(String key) throws CacheNotFoundException {
        try {
            String value = cacheDao.getUserById(key);
            logger.info("searching with key :: " + key + ", response received from cache :: " + value);
            if (value == null || value.length() == 0) {
                logger.error("key " + key + " Not Found");
                throw new CacheNotFoundException("key " + key + " Not Found");
            }
            return value;
        }catch(CacheNotFoundException e){
            logger.error("Exception while fetching value by key");
            throw e;
        }
    }

    /* Deletes a value from the cache using the specified key*/
    @Override
    public void delete(String key) throws CacheNotFoundException {
        try {
            Boolean status = cacheDao.delete(key);
            logger.info("deleting key :: " + key + ", response received from cache :: " + status);
            if (!status) {
                logger.error("key " + key + " NotFound");
                throw new CacheNotFoundException("key " + key + " NotFound");
            }
        } catch (CacheNotFoundException e) {
            logger.error("Cache not found: " + e.getMessage());
            throw e;
        }
    }

    /* Saves or updates a value in the cache with the given key and value*/
    @Override
    public void saveOrUpdate(String key, String value) throws UnableToAddKeyException {
        try {
            cacheDao.saveOrUpdate(key, value);
            logger.info("added key :: " + key);
        } catch (Exception e) {
            logger.error("key" + key + " Unable To Save");
            throw new UnableToAddKeyException("key" + key + " Unable To Save");
        }
    }
}
