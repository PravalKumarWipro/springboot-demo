package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.model.User;
import com.cacheing.cacheingtest.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import com.cacheing.cacheingtest.AppConstants;

@RestController
public class UserController {
	@Value("${cache.client:APACHE_IGNITE}")
    String cacheClient;
    String dataUseTest = "Sample===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Test";
    @Autowired
    public UserServiceImpl userServiceImpl;

    @GetMapping("/user/test")
    public String hello() {
        return "Hello From SpringBoot!!! Cache Client we are using =>  "+cacheClient;
    }


    @GetMapping("/user/{userId}")
    public String getBooks(@PathVariable("userId") int userId) {
        return userServiceImpl.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteBook(@PathVariable("userId") int userId) {
        userServiceImpl.delete(userId);
    }

    @PostMapping("/user/")
    public String addUser(@RequestBody User user) {
        userServiceImpl.saveOrUpdate(user.getUserId(), user.getUserName());
        return "User with userId " + user.getUserId() + " Added";
    }
}
