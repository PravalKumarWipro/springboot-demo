package com.cacheing.cacheingtest.dao;

public interface GenericCacheClient {
    String getValueById(int key);

    Boolean delete(int key);

    void saveOrUpdate(int key, String value);
}
