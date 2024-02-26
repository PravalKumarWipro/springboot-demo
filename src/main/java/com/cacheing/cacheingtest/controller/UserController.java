package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.dao.UserDao;
import com.cacheing.cacheingtest.model.User;
import com.cacheing.cacheingtest.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class UserController {

    @Autowired
    public UserServiceImpl userServiceImpl;

    @Autowired
    private UserDao userDao;

    @GetMapping("/user/test")
    public String testApi(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return "Hello From SpringBoot!!! Cache Client we are using  :: "+userDao.getClient(token);
    }


    @GetMapping("/user/{key}")
    public String getUser(@PathVariable("key") int key,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return userServiceImpl.getUserById(key,token);
    }

    @DeleteMapping("/{key}")
    public void deleteUser(@PathVariable("key") int key,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        userServiceImpl.delete(key,token);
    }

    @PostMapping("/user/")
    public String addUser(@RequestBody User user,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        userServiceImpl.saveOrUpdate(user.getKey(), user.getValue(),token);
        return "User with key " + user.getKey() + " Added";
    }
}
