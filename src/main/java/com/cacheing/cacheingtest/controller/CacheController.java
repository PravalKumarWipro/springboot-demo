package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.dao.CacheDao;
import com.cacheing.cacheingtest.dao.UnleashCustomClient;
import com.cacheing.cacheingtest.model.CacheMap;
import com.cacheing.cacheingtest.model.Response;
import com.cacheing.cacheingtest.service.CacheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class CacheController {

    @Autowired
    public CacheServiceImpl userServiceImpl;

    @Autowired
    private CacheDao cacheDao;

    @GetMapping("/caching/test")
    public Response testApi() {
        Response response = new Response(AppConstants.SUCCESS);
        response.setMessage("Cache Client :: " + cacheDao.getClient());
        return response;
    }


    @GetMapping("/caching/{key}")
    public Response getKey(@PathVariable("key") int key) {
        Response response = new Response(AppConstants.SUCCESS);
        response.setKey(key);
        response.setValue(userServiceImpl.getValueByKey(key));
        return response;
    }

    @DeleteMapping("/caching/{key}")
    public Response deleteKey(@PathVariable("key") int key) {
        userServiceImpl.delete(key);
        Response response = new Response(AppConstants.SUCCESS);
        response.setMessage("key " + key + " removed");
        return response;
    }

    @PostMapping("/caching/")
    public Response addKey(@RequestBody CacheMap cacheMap) {
        Response response = new Response(AppConstants.SUCCESS);
        userServiceImpl.saveOrUpdate(cacheMap.getKey(), cacheMap.getValue());
        response.setKey(cacheMap.getKey());
        response.setValue(cacheMap.getValue());
        response.setMessage("key " + cacheMap.getKey() + " added");
        return response;
    }

    @PostMapping("/unleash/token/{token}")
    public Response setUnleashToken(@PathVariable("token") String token) {
        Response response = new Response(AppConstants.SUCCESS);
        if (!StringUtils.isEmpty(token)) {
            UnleashCustomClient.token = token;
            response.setMessage("Token Updated");
        } else {
            response.setMessage("Invalid Token");
        }
        return response;
    }

    @GetMapping("/unleash/token")
    public Response getUnleashToken() {
        Response response = new Response(AppConstants.SUCCESS);
        response.setMessage("token : " + UnleashCustomClient.token);
        return response;
    }
}
