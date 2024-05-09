package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.ApacheIgniteClient;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.KeyNotExistsException;
import com.caching.cachingtest.model.CacheMap;
import com.caching.cachingtest.model.Response;
import com.caching.cachingtest.service.CacheServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/* This class handles all Cache CRUD operations */
@RestController
@Lazy
public class CacheController {

    @Autowired(required = false)
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
    public ResponseEntity<Response> testApi(HttpServletRequest request) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if(cacheDao.getClient() instanceof ApacheIgniteClient){
                response.setMessage("Cache Client :: " + cacheDao.getClient()+"  -   RebalancingMode :: "+apacheIgniteClient.getCacheRebalanceingModeFromConfig());
            }else{
                response.setMessage("Cache Client :: " + cacheDao.getClient());
            }
            LOGGER.info("Cache client Configured : {}\t ::Req Path : {}", cacheDao.getClient(),request.getRequestURI());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus("Internal Server Error");
            response.setMessage("Error occurred : " + e.getMessage());
            LOGGER.error("In testApi() Error while connecting to client :: {} \t stacktrace : {} \t:: Req Path :{}",e.getMessage(), Arrays.toString(e.getStackTrace()),request.getRequestURI());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /***
     * Endpoint to retrieve a value from the cache based on a key
     * @param key
     * @return
     */
    @GetMapping("/caching/{key}")
    public ResponseEntity<Response> getKey(HttpServletRequest request, @PathVariable("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.info("In getKey() fetching value with key : {} :: Req Path : {}, remoteAddress : {}", key, request.getRequestURI(),request.getRemoteAddr());
            response.setKey(key);
            response.setValue(userServiceImpl.getValueByKey(key));
            LOGGER.info("In getKey() value found with key {} :: Req Path : {} ", key, request.getRequestURI());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error("In getKey() Error occurred while fetching the value for key: {}, \t stacktrace : {} \t :: {}", key, Arrays.toString(e.getStackTrace()), request.getRequestURI());
            response.setStatus("Not Found");
            response.setMessage("Error occurred : "+e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
    }

    /* Endpoint to delete a value from the cache based on a key */
    @DeleteMapping("/caching/{key}")
    public ResponseEntity<Response> deleteKey(HttpServletRequest request, @PathVariable("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.info("In deleteKey() key : {} \t Req Path :: {} , remoteAddress :: {}", key, request.getRequestURI(),request.getRemoteAddr());
            userServiceImpl.delete(key);
            response.setMessage("key " + key + " removed");
            LOGGER.info("In deleteKey() Successfully removed key : {} \t Req Path :: {}", key, request.getRequestURI());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("In deleteKey() Error occurred while deleting key: {}\t stacktrace : {} \t Req Path :: {}", key, Arrays.toString(e.getStackTrace()),request.getRequestURI());
            response.setStatus("Not Found");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
    }

    /* Endpoint to add or update a key in the cache */
    @PostMapping("/caching/")
    public ResponseEntity<Response> addKey(HttpServletRequest request, @RequestBody CacheMap cacheMap) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.debug("In addKey() Key : {} added \t Req Path : {}, payload :: {}, remoteaddress :: {}",cacheMap.getKey(), request.getRequestURI(),(new ObjectMapper()).writeValueAsString(cacheMap),request.getRemoteAddr());
            LOGGER.info("In addKey() key : {} \t Req Path :{}", cacheMap.getKey(), request.getRequestURI());
            if (cacheMap.getTtl() == null) {
                cacheMap.setTtl(Long.MAX_VALUE);
            }
            userServiceImpl.save(cacheMap);
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " added");
            LOGGER.info("In addKey() Key : {} added \t Req Path : {}",cacheMap.getKey(), request.getRequestURI());
            LOGGER.debug("In addKey() Key : {} added \t Req Path : {}, payload :: {}, response :: {}",cacheMap.getKey(), request.getRequestURI(),(new ObjectMapper()).writeValueAsString(cacheMap), response.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        }catch (KeyExistsException keyExistsException){
            LOGGER.error("In addKey() key : {} already existing in cache\t exception : {}\t stacktrace :{} \t Req Path :: {}",cacheMap.getKey(),keyExistsException.getMessage(),Arrays.toString(keyExistsException.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage(keyExistsException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (InvalidTTLException invalidTTLException){
            LOGGER.error("In addKey() key : {} invalid ttl\t exception : {}\t stacktrace : {} \t Req Path :: {}",cacheMap.getKey(),invalidTTLException.getMessage(),Arrays.toString(invalidTTLException.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage(invalidTTLException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            LOGGER.error("In addKey() key : {} exception : {}\t stacktrace : {} \t Req Path :: {}",cacheMap.getKey(),e.getMessage(),Arrays.toString(e.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Endpoint to update a key in the cache */
    @PutMapping("/caching/")
    public ResponseEntity<Response> updateKey(HttpServletRequest request, @RequestBody CacheMap cacheMap) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.debug("In updateKey() Key : {} updated \t Req Path : {}, payload :: {}",cacheMap.getKey(), request.getRequestURI(),(new ObjectMapper()).writeValueAsString(cacheMap));
            LOGGER.info("In updateKey() key : {} \t Req Path :{}", cacheMap.getKey(), request.getRequestURI());
            if (cacheMap.getTtl() == null) {
                cacheMap.setTtl(Long.MAX_VALUE);
            }
            userServiceImpl.update(cacheMap);
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " updated");
            LOGGER.info("In updateKey() Key : {} updated \t Req Path : {}",cacheMap.getKey(), request.getRequestURI());
            LOGGER.debug("In updateKey() Key : {} updated \t Req Path : {}, payload :: {}, response :: {}",cacheMap.getKey(), request.getRequestURI(),(new ObjectMapper()).writeValueAsString(cacheMap), response.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        }catch (KeyNotExistsException keyNotExistsException){
            LOGGER.error("In updateKey() key : {} already existing in cache\t exception : {}\t stacktrace :{} \t Req Path :: {}",cacheMap.getKey(),keyNotExistsException.getMessage(),Arrays.toString(keyNotExistsException.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage(keyNotExistsException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (InvalidTTLException invalidTTLException){
            LOGGER.error("In updateKey() key : {} invalid ttl\t exception : {}\t stacktrace : {} \t Req Path :: {}",cacheMap.getKey(),invalidTTLException.getMessage(),Arrays.toString(invalidTTLException.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage(invalidTTLException.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            LOGGER.error("In updateKey() key : {} exception : {}\t stacktrace : {} \t Req Path :: {}",cacheMap.getKey(),e.getMessage(),Arrays.toString(e.getStackTrace()), request.getRequestURI());
            response.setStatus("Bad Request");
            response.setMessage("Error occurred : " + e.getMessage());
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
