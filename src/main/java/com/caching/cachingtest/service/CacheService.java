package com.caching.cachingtest.service;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyAlreadyExistsException;

/* Defines methods required for caching operations*/
public interface CacheService {

    String getValueByKey(String key) throws CacheNotFoundException;

    void delete(String key) throws CacheNotFoundException;

    void saveOrUpdate(String key, String value) throws KeyAlreadyExistsException;
}
