package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.exception.UserAlreadyExistsException;
import com.cacheing.cacheingtest.exception.UserNotFoundException;

public interface UserService {

    String getUserById(int userId) throws UserNotFoundException;

    void delete(int userId) throws UserNotFoundException;

    void saveOrUpdate(int userId, String userName) throws UserAlreadyExistsException;
}
