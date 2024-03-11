package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import org.junit.Assert;
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
            String userId = "test123";
            String userName = "abc";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.get(userId)).thenReturn(userName);
            String retrievedUserName = redisClient.getValueById(userId);
            assertEquals(userName, retrievedUserName);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).get(userId);
        }
        @Test
        public void testGetUserById_Failure(){
            String userId = "test456";
            when(redissonClient.getMapCache("Users")).thenThrow(new CacheNotFoundException("User not found in cache for key: "));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.getValueById(userId));
        }

        @Test
        public void testDelete_Success() {
            String userId = "test456";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.remove(userId)).thenReturn("John Doe");
            Boolean deletionResult = redisClient.delete(userId);
            assertTrue(deletionResult);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).remove(userId);
        }

        @Test
        public void testDelete_UserNotFound() {
            String userId = "test456";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            when(userCache.remove(userId)).thenReturn(null);
            Boolean deletionResult = redisClient.delete(userId);
            assertFalse(deletionResult);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).remove(userId);
        }
        @Test
        public void testDelete_Failure(){
            String userId = "test456";
            when(redissonClient.getMapCache("Users")).thenThrow(new CacheNotFoundException("Error while deleting a key"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.delete(userId));
        }

        @Test
        public void testSaveOrUpdate_Success() {
            String userId = "test456";
            String userName = "Test";
            doReturn(userCache).when(redissonClient).getMapCache("Users");
            redisClient.saveOrUpdate(userId, userName);
            verify(redissonClient).getMapCache("Users");
            verify(userCache).put(userId, userName);
        }
        @Test
        public void testSaveOrUpdate_Failure(){
            String userId = "test456";
            String userName = "Test";
            when(redissonClient.getMapCache("Users")).thenThrow(new CacheNotFoundException("Error while saving or updating cache"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.saveOrUpdate(userId,userName));
        }
}

