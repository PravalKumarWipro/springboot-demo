package com.caching.igniteexternaldb.controller;

import com.caching.igniteexternaldb.AppConstants;
import com.caching.igniteexternaldb.model.Person;
import com.caching.igniteexternaldb.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/* This class handles all Cache CRUD operations */
@RestController
public class CacheController {



    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);





    public ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public Ignite ignite;
    /***
     * Endpoint to test the Client
     * @return
     */
    @GetMapping("/caching/{key}")
    public ResponseEntity<Response> getKey(HttpServletRequest request, @PathVariable Integer key) {
        System.out.println("ignite created :: "+ignite);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");

        personCache.loadCache(null);
        Person person = personCache.get(key);
        System.out.println("person fetched:: "+person);
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if(person != null) {
                response.setValue(mapper.writeValueAsString(person));
            }else{
                response.setStatus("failed");
                response.setMessage("key : "+key+" not found");
                return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    /***
     * Endpoint to test the Client
     * @return
     */
    @DeleteMapping("/caching/{key}")
    public ResponseEntity<Response> deleteKey(HttpServletRequest request, @PathVariable Integer key) {
        System.out.println("ignite created :: "+ignite);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");

        personCache.loadCache(null);
        Response response = new Response(AppConstants.SUCCESS);
        Boolean status = personCache.remove(key);
        try {
            if(status) {
                response.setValue("key : "+key+" removed");
            }else{
                response.setStatus("failed");
                response.setMessage("key : "+key+" not found");
                return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("/caching")
    public ResponseEntity<Response> addKey(HttpServletRequest request, @RequestBody Person person) {
        Response response = new Response(AppConstants.SUCCESS);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");
        Person person1 = personCache.get(person.getId());
        if(person1 != null){
            response.setValue("key : "+person.getId()+" already exists cannot continue!!");
            return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
        }else{
            response.setValue("key : "+person.getId()+" added");
        }
        response.setKey(person.getId()+"");
        personCache.put(person.getId(),person);
        System.out.println("person added :: "+person);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PutMapping("/caching")
    public ResponseEntity<Response> updateKey(HttpServletRequest request, @RequestBody Person person) {
        Response response = new Response(AppConstants.SUCCESS);
        IgniteCache<Integer, Person> personCache = ignite.cache("PersonCache");
        Person person1 = personCache.get(person.getId());
        if(person1 == null){
            response.setValue("key : "+person.getId()+" not exists cannot continue!!");
            return new ResponseEntity<Response>(response, HttpStatus.FORBIDDEN);
        }else{
            response.setValue("key : "+person.getId()+" updated");
        }
        response.setKey(person.getId()+"");
        personCache.put(person.getId(),person);
        System.out.println("person updated :: "+person);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}
