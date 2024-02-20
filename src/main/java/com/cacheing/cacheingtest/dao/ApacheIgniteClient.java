package com.cacheing.cacheingtest.dao;

import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApacheIgniteClient implements GenericCacheClient{

    @Autowired
    IgniteClient igniteClient;

    String CACHE_NAME = "Users";


    public String getUserById(int userId) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        String userName = clientCache.get(userId);
        System.out.println("APACHE IGNITE >>> searching user with userId :: " + userId + ", response received from cache :: " + userName);
        return userName;
    }

    public Boolean delete(int userId) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        Boolean status = clientCache.remove(userId);
        System.out.println("APACHE IGNITE >>> response after deletion :: " + status);
        return status;
    }

    public void saveOrUpdate(int userId, String userName) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        System.out.println("APACHE IGNITE >>> added user with userId :: " + userId);
        clientCache.put(userId, userName);
    }
}
