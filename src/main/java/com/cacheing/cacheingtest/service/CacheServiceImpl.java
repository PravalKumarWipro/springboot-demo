package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.dao.CacheDao;
import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import com.cacheing.cacheingtest.exception.UnableToAddKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    public CacheDao cacheDao;


    @Override
    public String getValueByKey(int key) throws CacheNotFoundException {
        String value = cacheDao.getUserById(key);
        System.out.println("searching with key :: " + key + ", response received from cache :: " + value);
        if (value == null || value.length() == 0) {
            throw new CacheNotFoundException("key " + key + " NotFound");
        }
        return value;
    }

    @Override
    public void delete(int key) throws CacheNotFoundException {
        try {
            Boolean status = cacheDao.delete(key);
            System.out.println("deleting key :: " + key + ", response received from cache :: " + status);
            if (!status) {
                throw new CacheNotFoundException("key " + key + " NotFound");
            }
        } catch (CacheNotFoundException e) {
            System.err.println("Cache not found: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void saveOrUpdate(int key, String value) throws UnableToAddKeyException {
        try {
            cacheDao.saveOrUpdate(key, value);
            System.out.println("added key :: " + key);
        } catch (Exception e) {
            throw new UnableToAddKeyException("key" + key + " Unable To Save");
        }
    }
}
