package com.cacheing.cacheingtest.dao;

import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApacheIgniteClient implements GenericCacheClient {

    @Autowired
    IgniteClient igniteClient;

    String CACHE_NAME = "Users";


    public String getValueById(int key) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        String value = clientCache.get(key);
        System.out.println("APACHE IGNITE >>> searching user with key :: " + key + ", response received from cache :: " + value);
        return value;
    }

    public Boolean delete(int key) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        Boolean status = clientCache.remove(key);
        System.out.println("APACHE IGNITE >>> response after deletion :: " + status);
        return status;
    }

    public void saveOrUpdate(int key, String value) {
        ClientCache<Integer, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
        System.out.println("APACHE IGNITE >>> added user with key :: " + key);
        clientCache.put(key, value);
    }

    @Override
    public String toString() {
        return "ApacheIgniteClient";
    }
}
