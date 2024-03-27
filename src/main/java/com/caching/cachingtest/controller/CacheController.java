package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.ApacheIgniteClient;
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

import java.util.Arrays;

/* This class handles all Cache CRUD operations */
@RestController
public class CacheController {

    @Autowired
    public CacheServiceImpl userServiceImpl;

    @Autowired
    private CacheDao cacheDao;

    @Autowired
    private ApacheIgniteClient apacheIgniteClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);


    /***
     * Endpoint to test the Client
     * @return
     */
    @GetMapping("/caching/test")
    public ResponseEntity<Response> testApi() {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if(cacheDao.getClient() instanceof ApacheIgniteClient){
                response.setMessage("Cache Client :: " + cacheDao.getClient()+"  -   RebalancingMode :: "+apacheIgniteClient.getCacheRebalanceingModeFromConfig());
            }else{
                response.setMessage("Cache Client :: " + cacheDao.getClient());
            }
            LOGGER.info("In testApi() Cache client Configured : {}", cacheDao.getClient());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus("Internal Server Error");
            response.setMessage("Error occurred : " + e.getMessage());
            LOGGER.error("In testApi() Error while connecting to client :: {} \t stacktrace : {}",e.getMessage(), Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /***
     * Endpoint to retrieve a value from the cache based on a key
     * @param key
     * @return
     */
    @GetMapping("/caching/{key}")
    public ResponseEntity<Response> getKey(@PathVariable("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.info("In getKey() fetching value with key : {} ", key);
            response.setKey(key);
            response.setValue(userServiceImpl.getValueByKey(key));
            LOGGER.info("In getKey() value found with key {} ", key);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("In getKey() Error occurred while fetching the value for key: {}, \t stacktrace : {}", key, Arrays.toString(e.getStackTrace()));
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
            LOGGER.info("In deleteKey() key : {} ", key);
            userServiceImpl.delete(key);
            response.setMessage("key " + key + " removed");
            LOGGER.info("In deleteKey() Successfully removed key : {} ", key);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("In deleteKey() Error occurred while deleting key: {}\t stacktrace : {}", key, Arrays.toString(e.getStackTrace()));
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
            LOGGER.info("In addKey() key : {} ", cacheMap.getKey());
            if (cacheMap.getTtl() == null) {
                cacheMap.setTtl(Long.MAX_VALUE);
            }
            userServiceImpl.saveOrUpdate(cacheMap);
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " added");
            LOGGER.info("In addKey() Key : {} added",cacheMap.getKey());
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        }catch (KeyExistsException keyExistsException){
            LOGGER.error("In addKey() key : {} already existing in cache\t exception : {}\t stacktrace : ",cacheMap.getKey(),keyExistsException.getMessage(),Arrays.toString(keyExistsException.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage(keyExistsException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (InvalidTTLException invalidTTLException){
            LOGGER.error("In addKey() key : {} invalid ttl\t exception : {}\t stacktrace : ",cacheMap.getKey(),invalidTTLException.getMessage(),Arrays.toString(invalidTTLException.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage(invalidTTLException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            LOGGER.error("In addKey() key : {} exception : {}\t stacktrace : ",cacheMap.getKey(),e.getMessage(),Arrays.toString(e.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
