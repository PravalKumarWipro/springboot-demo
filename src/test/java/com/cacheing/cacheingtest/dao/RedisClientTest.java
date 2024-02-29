package com.cacheing.cacheingtest.dao;

import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisClientTest {
        @Mock
        private RedissonClient redissonClient;
        @Mock
        private RMapCache<String, String> userCache;
        @InjectMocks
        private RedisClient redisClient;

        @Test
        public void testGetUserById_Success() throws CacheNotFoundException {
            int userId = 123;
            String userName = "abc";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.get(String.valueOf(userId))).thenReturn(userName);
            String retrievedUserName = redisClient.getValueById(userId);
            assertEquals(userName, retrievedUserName);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).get(String.valueOf(userId));
        }
        @Test
        public void testDelete_Success() {
            int userId = 789;
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.remove(String.valueOf(userId))).thenReturn("John Doe");
            Boolean deletionResult = redisClient.delete(userId);
            assertTrue(deletionResult);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).remove(String.valueOf(userId));
        }

        @Test
        public void testDelete_UserNotFound() {
            int userId = 123456;
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.remove(String.valueOf(userId))).thenReturn(null);
            Boolean deletionResult = redisClient.delete(userId);
            assertFalse(deletionResult);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).remove(String.valueOf(userId));
        }

        @Test
        public void testSaveOrUpdate() {
            int userId = 131415;
            String userName = "Test";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            redisClient.saveOrUpdate(userId, userName);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).put(String.valueOf(userId), userName);
        }
    }
