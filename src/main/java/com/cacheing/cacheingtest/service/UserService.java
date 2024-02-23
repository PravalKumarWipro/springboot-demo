package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.exception.UserAlreadyExistsException;
import com.cacheing.cacheingtest.exception.UserNotFoundException;

public interface UserService {

    String getUserById(int userId,String token) throws UserNotFoundException;

    void delete(int userId,String token) throws UserNotFoundException;

    void saveOrUpdate(int userId, String userName,String token) throws UserAlreadyExistsException;
}
