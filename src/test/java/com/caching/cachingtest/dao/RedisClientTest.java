package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.KeyNotExistsException;
import com.caching.cachingtest.exception.KeyNotExistsExceptionTest;
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
            String key = "test456";
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.remove(key)).thenReturn("Testing");
            Boolean deletionResult = redisClient.delete(key);
            assertTrue(deletionResult);
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).remove(key);
        }

        @Test
        public void testDelete_UserNotFound() {
            String key = "test456";
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.remove(key)).thenReturn(null);
            Boolean deletionResult = redisClient.delete(key);
            assertFalse(deletionResult);
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).remove(key);
        }
        @Test
        public void testDelete_Failure(){
            String key = "test456";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("Error while deleting a key"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.delete(key));
        }

        @Test
        public void testSave_Success() {
            String key = "test456";
            String value = "Test";
            long ttl = 30;
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            redisClient.save(new CacheMap(key, value,30L));
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).put(key, value,ttl, TimeUnit.SECONDS);
        }
        @Test
        public void testSave_Failure(){
            String key = "test456";
            String value = "Test";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("Error while saving or updating cache"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.save(new CacheMap(key,value,30L)));
        }
        @Test
        public void testSave_KeyExistsException(){
            CacheMap existingCacheMap = new CacheMap("existingKey", "existingValue", 30l);
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.get(existingCacheMap.getKey())).thenReturn("existingValue");
            Assert.assertThrows(KeyExistsException.class, () -> redisClient.save(existingCacheMap));
        }
        @Test
        public void testUpdate_Success() {
            String key = "test456";
            String updatedvalue = "Test";
            long ttl = 30;
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.get(key)).thenReturn("existingValue");
            redisClient.update(new CacheMap(key, updatedvalue,30L));
            verify(redissonClient).getMapCache(cacheName);
            verify(userCache).put(key, updatedvalue,ttl, TimeUnit.SECONDS);
        }
        @Test
        public void testUpdate_Failure(){
            String key = "test456";
            String updatedvalue = "Test";
            when(redissonClient.getMapCache(cacheName)).thenThrow(new CacheNotFoundException("Error while saving or updating cache"));
            Assert.assertThrows(CacheNotFoundException.class,()->redisClient.update(new CacheMap(key,updatedvalue,30L)));
        }
        @Test
        public void testUpdate_KeyNotExistsException(){
            CacheMap existingCacheMap = new CacheMap("existingKey", "updatedValue", 30l);
            doReturn(userCache).when(redissonClient).getMapCache(cacheName);
            when(userCache.get(existingCacheMap.getKey())).thenReturn(null);
            Assert.assertThrows(KeyNotExistsException.class, () -> redisClient.update(existingCacheMap));
        }
}

