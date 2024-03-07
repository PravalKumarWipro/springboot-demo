package com.caching.cachingtest.dao;

/* This interface defines the methods to interact with Cache */
public interface GenericCacheClient {
    String getValueById(int key);

    Boolean delete(int key);

    void saveOrUpdate(int key, String value);
}
