package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import com.caching.cachingtest.model.Response;
import com.caching.cachingtest.service.CacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* This class handles all Cache CRUD operations */
@RestController
public class CacheController {

    @Autowired
    public CacheServiceImpl userServiceImpl;

    @Autowired
    private CacheDao cacheDao;

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    /* Endpoint to test the Client */
    @GetMapping("/caching/test")
    public ResponseEntity<Response> testApi() {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            response.setMessage("Cache Client :: " + cacheDao.getClient());
            logger.info("Cache Client : " + cacheDao.getClient());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus("Internal Server Error");
            response.setMessage("Error occurred : " + e.getMessage());
            logger.error("Error while fetching the Client");
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Endpoint to retrieve a value from the cache based on a key */
    @GetMapping("/caching/{key}")
    public ResponseEntity<Response> getKey(@PathVariable("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            response.setKey(key);
            response.setValue(userServiceImpl.getValueByKey(key));
            logger.info("Key : "+response.getKey()+"Value : "+response.getValue());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error("Error occurred while fetching the value for key: "+key);
            response.setStatus("Not Found");
            response.setMessage("Error occurred : "+e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
    }

    /* Endpoint to delete a value from the cache based on a key */
    @DeleteMapping("/caching/{key}")
    public ResponseEntity<Response> deleteKey(@PathVariable("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            userServiceImpl.delete(key);
            response.setMessage("key " + key + " removed");
            logger.info("Successfully removed key : " + key);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while deleting key: " + key);
            response.setStatus("Not Found");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
    }

    /* Endpoint to add or update a key in the cache */
    @PostMapping("/caching/")
    public ResponseEntity<Response> addKey(@RequestBody CacheMap cacheMap) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if (cacheMap.getTtl() == null) {
                cacheMap.setTtl(Long.MAX_VALUE);
            }
            userServiceImpl.saveOrUpdate(cacheMap);
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " added");
            logger.info("Key " + cacheMap.getKey() + " added/Updated");
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        }catch (KeyExistsException keyExistsException){
            logger.error("key  " + cacheMap.getKey() + "already existing in cache ");
            response.setStatus("Bad Request");
            response.setMessage(keyExistsException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (InvalidTTLException invalidTTLException){
            logger.error("key  " + cacheMap.getKey() + "invalid ttl");
            response.setStatus("Bad Request");
            response.setMessage(invalidTTLException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("Error while adding a key");
            response.setStatus("Bad Request");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
