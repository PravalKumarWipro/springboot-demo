package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.dao.UserDao;
import com.cacheing.cacheingtest.exception.UnableToAddUserException;
import com.cacheing.cacheingtest.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserDao userDao;

    

    @Override
    public String getUserById(int userId,String token) throws UserNotFoundException {
        String userName = userDao.getUserById(userId,token);
        System.out.println("searching user with userId :: " + userId + ", response received from cache :: " + userName);
        if (userName == null || userName.length() == 0) {
            throw new UserNotFoundException(userId + " UserNotFound");
        }
        return userName + " : " + userId;
    }

    @Override
    public void delete(int userId,String token) throws UserNotFoundException {
        Boolean status = userDao.delete(userId,token);
        System.out.println("deleting user with userId :: " + userId + ", response received from cache :: " + status);
        if (!status) {
            throw new UserNotFoundException(userId + " UserNotFound");
        }
    }

    @Override
    public void saveOrUpdate(int userId, String userName,String token) throws UnableToAddUserException {
        try {
            userDao.saveOrUpdate(userId, userName,token);
            System.out.println("added user with userId :: " + userId);
        } catch (Exception e) {
            throw new UnableToAddUserException(userId + " Unable To Save");
        }
    }
}
