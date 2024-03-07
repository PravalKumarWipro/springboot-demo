package com.caching.cachingtest.service;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyAlreadyExistsException;

/* Defines methods required for caching operations*/
public interface CacheService {

    String getValueByKey(int key) throws CacheNotFoundException;

    void delete(int key) throws CacheNotFoundException;

    void saveOrUpdate(int key, String value) throws KeyAlreadyExistsException;
}
