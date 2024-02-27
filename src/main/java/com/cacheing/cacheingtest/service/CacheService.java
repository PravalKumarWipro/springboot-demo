package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import com.cacheing.cacheingtest.exception.KeyAlreadyExistsException;

public interface CacheService {

    String getValueByKey(int key) throws CacheNotFoundException;

    void delete(int key) throws CacheNotFoundException;

    void saveOrUpdate(int key, String value) throws KeyAlreadyExistsException;
}
