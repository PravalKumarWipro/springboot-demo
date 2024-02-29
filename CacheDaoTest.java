package com.cacheing.cacheingtest.dao;


import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheDaoTest {

    @InjectMocks
    @Spy
    private CacheDao cacheDao;

    @Mock
    private ApacheIgniteClient apacheIgniteClient;

    @Mock
    private RedisClient redisClient;

    @Mock
    private UnleashCustomClient unleashCustomClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_ApacheIgnite() throws CacheNotFoundException {
        int key = 123;
        String expectedUser = "Test";
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(false);
        when(apacheIgniteClient.getValueById(key)).thenReturn(expectedUser);
        String actualUser = cacheDao.getUserById(key);
        assertEquals(expectedUser, actualUser);
        verify(apacheIgniteClient, times(1)).getValueById(key);
        verify(redisClient, never()).getValueById(key);
    }

    @Test
    public void testGetUserById_Redis() throws CacheNotFoundException {
        int key = 456;
        String expectedUser = "Test";
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(true);

        when(redisClient.getValueById(key)).thenReturn(expectedUser);
        String actualUser = cacheDao.getUserById(key);
        assertEquals(expectedUser, actualUser);
        verify(apacheIgniteClient, never()).getValueById(key);
        verify(redisClient, times(1)).getValueById(key);
    }

    @Test
    public void testDelete_ApacheIgnite() {
        int key = 789;
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(false);
        when(cacheDao.delete(key)).thenReturn(true);
        boolean result = cacheDao.delete(key);
        assertTrue(result);
        verify(apacheIgniteClient, times(1)).delete(key);
        verify(redisClient, never()).delete(key);
    }

    @Test
    public void testDelete_Redis() {
        int key = 987;
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(true);
        when(cacheDao.delete(key)).thenReturn(true);
        boolean result = cacheDao.delete(key);
        assertTrue(result);
        verify(apacheIgniteClient, never()).delete(key);
        verify(redisClient, times(1)).delete(key);
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite() {
        int key = 111;
        String value = "Test";
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(false);
        cacheDao.saveOrUpdate(key, value);
        verify(apacheIgniteClient, times(1)).saveOrUpdate(key, value);
        verify(redisClient, never()).saveOrUpdate(key, value);
    }

    @Test
    public void testSaveOrUpdate_Redis() {
        int key = 222;
        String value = "Test";
        String token = "abc";
        when(unleashCustomClient.isRedisEnabled()).thenReturn(true);
        cacheDao.saveOrUpdate(key, value);
        verify(apacheIgniteClient, never()).saveOrUpdate(key, value);
        verify(redisClient, times(1)).saveOrUpdate(key, value);
    }
}
