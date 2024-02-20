package com.cacheing.cacheingtest.dao;

public interface GenericCacheClient {
    String getUserById(int userId);

    Boolean delete(int userId);

    void saveOrUpdate(int userId, String userName);
}
