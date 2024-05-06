package com.caching.igniteexternaldb.controller;

import com.caching.igniteexternaldb.AppConstants;
import com.caching.igniteexternaldb.dao.ApacheIgniteClient;
import com.caching.igniteexternaldb.exception.InvalidTTLException;
import com.caching.igniteexternaldb.exception.KeyExistsException;
import com.caching.igniteexternaldb.model.CacheMap;
import com.caching.igniteexternaldb.model.Person;
import com.caching.igniteexternaldb.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.client.IgniteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/* This class handles all Cache CRUD operations */
@RestController
public class CacheController {



    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);


//    @Autowired
//    public IgniteCache<Integer, Person> igniteCache;


    public ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public Ignite ignite;
    /***
     * Endpoint to test the Client
     * @return
     */
    @GetMapping("/caching/test/{key}")
    public ResponseEntity<Response> testApi(HttpServletRequest request, @PathVariable Integer key) {
        System.out.println("ignite created :: "+ignite);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");

        personCache.loadCache(null);
        Person person = personCache.get(key);
        System.out.println("person fetched:: "+person);
//        Person result =igniteCache.get(1);
//        System.out.println("person response :: "+result);
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if(person != null) {
                response.setValue(mapper.writeValueAsString(person));
            }else{
                response.setValue(null);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("/caching/testPost")
    public ResponseEntity<Response> testPost(HttpServletRequest request, @RequestBody Person person) {
        Response response = new Response(AppConstants.SUCCESS);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");

        personCache.put(person.getId(),person);
        System.out.println("person added :: "+person);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}
