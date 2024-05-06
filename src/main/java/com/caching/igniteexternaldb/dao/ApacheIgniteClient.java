package com.caching.igniteexternaldb.dao;

import com.caching.igniteexternaldb.exception.KeyExistsException;
import com.caching.igniteexternaldb.model.CacheMap;
import com.caching.igniteexternaldb.model.Person;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.cache.configuration.Factory;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*This class interacts with Apache Ignite Cache and performs all cache operations like save/Update, delete and getValueByKey*/
@Component
public class ApacheIgniteClient {
    /*private static final Logger LOGGER = LoggerFactory.getLogger(ApacheIgniteClient.class);

    @Autowired(required = false)
    IgniteClient igniteClient;

    @Value("${cache.name:Cache}")
    String cacheName;

    @Value("${cache.rebalance.mode:SYNC}")
    String cacheRebalanceMode;

    *//***
     * Retrieves the value associated with the given key from the cache
     * @param key
     * @return
     *//*
    public String getValueById(String key) {
        try {
            LOGGER.info("In getValueById() client ::: APACHE IGNITE searching with key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            String value = clientCache.get(key);
            LOGGER.info("In getValueById() client ::: APACHE IGNITE key :: {} found", key);
            LOGGER.debug("In getValueById() client ::: APACHE IGNITE key :: {} found, value ::{}", key,value);
            return value;
        } catch (Exception e) {
            LOGGER.error("In getValueById() client ::: APACHE IGNITE Error while searching with key :: {}",key);
            return null;
        }
    }


    *//***
     * Deletes the entry associated with the given key from the cache
     * @param key
     * @return
     *//*
    public Boolean delete(String key) {
        try {
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            Boolean status = clientCache.remove(key);
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {} success", key);
            return status;
        } catch (Exception e) {
            LOGGER.error("In delete() client ::: APACHE IGNITE Error while deleting key : {}  from ApacheClient exception : {}, stacktrace : {} ",key, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }


    *//***
     * Saves or updates the value associated with the given key in the cache
     * @param cacheMap
     *//*
    public void saveOrUpdate(CacheMap cacheMap) {

        CacheConfiguration<Integer, Person> clientConfig = getClientConfigs();
        ClientCacheConfiguration cacheConfiguration = new ClientCacheConfiguration();
        cacheConfiguration.setName(cacheName);
        CacheRebalanceMode cacheRebalanceMode = getCacheRebalanceingModeFromConfig();
        LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" trying adding the key : "+ cacheMap.getKey());
        cacheConfiguration.setRebalanceMode(cacheRebalanceMode);
        ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheConfiguration).withExpirePolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, cacheMap.getTtl())));
        clientCache.getConfiguration();
        String existingValue = clientCache.get(cacheMap.getKey());
        if(existingValue !=null){
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed as Key Already exists");
            throw new KeyExistsException("key "+cacheMap.getKey()+" already exists, cannot continue!");
        }
        try {
            clientCache.put(cacheMap.getKey(), cacheMap.getValue());
            LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" success");
            LOGGER.debug("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" success, value ::"+cacheMap.getValue());
        } catch (Exception e) {
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed Error : "+e.getMessage()+"\t stacktrace : "+ Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Error :  " + e.getMessage(), e);
        }
    }

    public CacheConfiguration<Integer, Person> getClientConfigs(){
        CacheConfiguration<Integer, Person> personCacheCfg = new CacheConfiguration<>();

        personCacheCfg.setName("PersonCache");
        personCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        personCacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);

        personCacheCfg.setReadThrough(true);
        personCacheCfg.setWriteThrough(true);

        CacheJdbcPojoStoreFactory<Integer, Person> factory = new CacheJdbcPojoStoreFactory<>();
        factory.setDialect(new MySQLDialect());
        factory.setDataSourceFactory((Factory<DataSource>)() -> {
            MysqlDataSource mysqlDataSrc = new MysqlDataSource();
            mysqlDataSrc.setURL("jdbc:mysql://172.17.0.2:3306/ignitedb");
            mysqlDataSrc.setUser("root");
            mysqlDataSrc.setPassword("root");
            return mysqlDataSrc;
        });

        JdbcType personType = new JdbcType();
        personType.setCacheName("PersonCache");
        personType.setKeyType(Integer.class);
        personType.setValueType(Person.class);
// Specify the schema if applicable
// personType.setDatabaseSchema("MY_DB_SCHEMA");
        personType.setDatabaseTable("Person");

        personType.setKeyFields(new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"));

        personType.setValueFields(
                new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"),
                new JdbcTypeField(java.sql.Types.VARCHAR, "name", String.class, "name"));

        factory.setTypes(personType);

        personCacheCfg.setCacheStoreFactory(factory);

        QueryEntity qryEntity = new QueryEntity();

        qryEntity.setKeyType(Integer.class.getName());
        qryEntity.setValueType(Person.class.getName());
        qryEntity.setKeyFieldName("id");

        Set<String> keyFields = new HashSet<>();
        keyFields.add("id");
        qryEntity.setKeyFields(keyFields);

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "java.lang.Integer");
        fields.put("name", "java.lang.String");

        qryEntity.setFields(fields);

        personCacheCfg.setQueryEntities(Collections.singletonList(qryEntity));
        return personCacheCfg;
    }

    *//***
     *
     * @return
     *//*
    @Override
    public String toString() {
        return "ApacheIgniteClient";
    }

    *//***
     * This method returns the using CacheRebalanceMode which is read from config
     * @return
     *//*

    public CacheRebalanceMode getCacheRebalanceingModeFromConfig(){
        CacheRebalanceMode finalCacheRebalanceMode = null;
        switch(cacheRebalanceMode){
            case "SYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
            case "ASYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.ASYNC;
                break;
            case "NONE":
                finalCacheRebalanceMode =  CacheRebalanceMode.NONE;
                break;
            default:
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
        }
        return finalCacheRebalanceMode;
    }*/
}
