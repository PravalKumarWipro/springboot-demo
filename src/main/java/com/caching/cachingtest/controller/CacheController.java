package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.model.CacheMap;
import com.caching.cachingtest.model.Response;
import com.caching.cachingtest.service.CacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/* This class handles all Caching related operations */
@RestController
public class CacheController {

    @Autowired
    public CacheServiceImpl userServiceImpl;

    @Autowired
    private CacheDao cacheDao;

    private static final Logger logger= LoggerFactory.getLogger(CacheController.class);

    /* Endpoint to test the Client */
    @GetMapping("/caching/test")
    public Response testApi() {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            response.setMessage("Cache Client :: " + cacheDao.getClient());
            logger.info("Cache Client : "+cacheDao.getClient());
        }catch (Exception e){
            response.setMessage("Error occurred : "+e.getMessage());
            logger.error("Error while fetching the Client");
        }
        return response;
    }

    /* Endpoint to retrieve a value from the cache based on a key */
    @GetMapping("/caching/{key}")
    public Response getKey(@PathVariable("key") int key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            response.setKey(key);
            response.setValue(userServiceImpl.getValueByKey(key));
            logger.info("Key : "+response.getKey()+"Value : "+response.getValue());
        }catch(Exception e){
            response.setMessage("Error occurred : "+e.getMessage());
            logger.error("Error occurred while fetching the value for key: "+key);
        }
        return response;
    }

    /* Endpoint to delete a value from the cache based on a key */
    @DeleteMapping("/caching/{key}")
    public Response deleteKey(@PathVariable("key") int key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            userServiceImpl.delete(key);
            response.setMessage("key " + key + " removed");
            logger.info("Successfully removed key : "+key);
        }catch(Exception e){
            response.setMessage("Error occurred : "+e.getMessage());
            logger.error("Error occurred while deleting key: "+key);
        }
        return response;
    }

    /* Endpoint to add or update a key in the cache */
    @PostMapping("/caching/")
    public Response addKey(@RequestBody CacheMap cacheMap) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            userServiceImpl.saveOrUpdate(cacheMap.getKey(), cacheMap.getValue());
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " added");
            logger.info("Key " + cacheMap.getKey()+ " added/Updated");
        }catch(Exception e){
            response.setMessage("Error occurred : "+e.getMessage());
            logger.error("Error while adding a key");
        }
        return response;
    }
}
