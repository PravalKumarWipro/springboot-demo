package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.dao.UserDao;
import com.cacheing.cacheingtest.model.User;
import com.cacheing.cacheingtest.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class UserController {

    @Autowired
    public UserServiceImpl userServiceImpl;

    @Autowired
    private UserDao userDao;

    @GetMapping("/user/test")
    public String hello() {
        return "Hello From SpringBoot!!! Cache Client we are using  :: "+userDao.getClient();
    }


    @GetMapping("/user/{key}")
    public String getBooks(@PathVariable("key") int key) {
        return userServiceImpl.getUserById(key);
    }

    @DeleteMapping("/{key}")
    public void deleteBook(@PathVariable("key") int key) {
        userServiceImpl.delete(key);
    }

    @PostMapping("/user/")
    public String addUser(@RequestBody User user) {
        userServiceImpl.saveOrUpdate(user.getKey(), user.getValue());
        return "User with key " + user.getKey() + " Added";
    }
}
