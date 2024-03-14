package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.model.CacheMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("classpath:test.properties")
public class RedisClientTest {
        @Mock
        private RedissonClient redissonClient;
        @Mock
        private RMapCache<String, String> userCache;
        @InjectMocks
        private RedisClient redisClient;
        @Value("${cache.name:Cache}")
        String cacheName;

        @Test
        public void testGetUserById_Success() throws CacheNotFoundException {
            String userId = "test123";
            String userName = "abc";
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.get(userId)).thenReturn(userName);
            String retrievedUserName = redisClient.getValueById(userId);
            assertEquals(userName, retrievedUserName);
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).get(userId);
        }
        @Test
        public void testGetUserById_Failure(){
            String userId = "test456";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("User not found in cache for key: "));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.getValueById(userId));
        }

        @Test
        public void testDelete_Success() {
            String userId = "test456";
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.remove(userId)).thenReturn("Testing");
            Boolean deletionResult = redisClient.delete(userId);
            assertTrue(deletionResult);
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).remove(userId);
        }

        @Test
        public void testDelete_UserNotFound() {
            String userId = "test456";
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.remove(userId)).thenReturn(null);
            Boolean deletionResult = redisClient.delete(userId);
            assertFalse(deletionResult);
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).remove(userId);
        }
        @Test
        public void testDelete_Failure(){
            String userId = "test456";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("Error while deleting a key"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.delete(userId));
        }

        @Test
        public void testSaveOrUpdate_Success() {
            String userId = "test456";
            String userName = "Test";
            long ttl = 30;
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            redisClient.saveOrUpdate(new CacheMap(userId, userName,30L));
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).put(userId, userName,ttl, TimeUnit.SECONDS);
        }
        @Test
        public void testSaveOrUpdate_Failure(){
            String userId = "test456";
            String userName = "Test";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("Error while saving or updating cache"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.saveOrUpdate(new CacheMap(userId,userName,30L)));
        }
}

