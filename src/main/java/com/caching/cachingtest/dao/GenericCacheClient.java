package com.caching.cachingtest.dao;

/* This interface defines the methods to interact with Cache */
public interface GenericCacheClient {
    String getValueById(String key);

    Boolean delete(String key);

    void saveOrUpdate(String key, String value);
}
