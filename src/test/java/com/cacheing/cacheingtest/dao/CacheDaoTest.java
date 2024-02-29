package com.cacheing.cacheingtest.dao;


import com.cacheing.cacheingtest.AppConstants;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_ApacheIgnite() throws CacheNotFoundException {
        int userId = 123;
        String expectedUser = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.getValueById(userId)).thenReturn(expectedUser);
        String actualUser = cacheDao.getUserById(userId);
        assertEquals(expectedUser, actualUser);
        verify(apacheIgniteClient, times(1)).getValueById(userId);
        verify(redisClient, never()).getValueById(userId);
    }

    @Test
    public void testGetUserById_Redis() throws CacheNotFoundException {
        int userId = 456;
        String expectedUser = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.getValueById(userId)).thenReturn(expectedUser);
        String actualUser = cacheDao.getUserById(userId);
        assertEquals(expectedUser, actualUser);
        verify(apacheIgniteClient, never()).getValueById(userId);
        verify(redisClient, times(1)).getValueById(userId);
    }

    @Test
    public void testDelete_ApacheIgnite() {
        int userId = 789;
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(cacheDao.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, times(1)).delete(userId);
        verify(redisClient, never()).delete(userId);
    }

    @Test
    public void testDelete_Redis() {
        int userId = 987;
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(cacheDao.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, never()).delete(userId);
        verify(redisClient, times(1)).delete(userId);
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite() {
        int userId = 111;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        cacheDao.saveOrUpdate(userId, userName);
        verify(apacheIgniteClient, times(1)).saveOrUpdate(userId, userName);
        verify(redisClient, never()).saveOrUpdate(userId, userName);
    }

    @Test
    public void testSaveOrUpdate_Redis() {
        int userId = 222;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        cacheDao.saveOrUpdate(userId, userName);
        verify(apacheIgniteClient, never()).saveOrUpdate(userId, userName);
        verify(redisClient, times(1)).saveOrUpdate(userId, userName);
    }
}
