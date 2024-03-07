package com.caching.cachingtest.dao;


import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
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
    public void testGetUserById_ApacheIgnite_Success() throws CacheNotFoundException {
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
    public void testGetUserById_ApacheIgnite_Failure(){
        int userId = 123;
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.getValueById(userId)).thenThrow(new CacheNotFoundException("Cache not found"));
        Assert.assertThrows(CacheNotFoundException.class,()-> cacheDao.getUserById(userId));
    }

    @Test
    public void testGetUserById_Redis_Success() throws CacheNotFoundException {
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
    public void testGetUserById_Redis_Failure(){
        int userId = 123;
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.getValueById(userId)).thenThrow(new CacheNotFoundException("Cache not found"));
        Assert.assertThrows(CacheNotFoundException.class,()-> cacheDao.getUserById(userId));
    }

    @Test
    public void testDelete_ApacheIgnite_Success() {
        int userId = 789;
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, times(1)).delete(userId);
        verify(redisClient, never()).delete(userId);
    }

    @Test
    public void testDelete_ApacheIgnite_Failure(){
        int userId = 789;
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.delete(userId)).thenThrow(new CacheNotFoundException("Failed to delete key from cache with exception : "));
        Assert.assertThrows(CacheNotFoundException.class,()->cacheDao.delete(userId));
    }

    @Test
    public void testDelete_Redis_Success() {
        int userId = 987;
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, never()).delete(userId);
        verify(redisClient, times(1)).delete(userId);
    }

    @Test
    public void testDelete_Redis_Failure(){
        int userId = 987;
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.delete(userId)).thenThrow(new CacheNotFoundException("Failed to delete key from cache with exception : "));
        Assert.assertThrows(CacheNotFoundException.class,()->cacheDao.delete(userId));
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite_Success() {
        int userId = 111;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        cacheDao.saveOrUpdate(userId, userName);
        verify(apacheIgniteClient, times(1)).saveOrUpdate(userId, userName);
        verify(redisClient, never()).saveOrUpdate(userId, userName);
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite_Failure(){
        int userId = 111;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        doThrow(new UnableToAddKeyException("Unable to add key")).when(apacheIgniteClient).saveOrUpdate(userId,userName);
        assertThrows(UnableToAddKeyException.class,()->cacheDao.saveOrUpdate(userId,userName));
    }

    @Test
    public void testSaveOrUpdate_Redis_Success() {
        int userId = 222;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        cacheDao.saveOrUpdate(userId, userName);
        verify(apacheIgniteClient, never()).saveOrUpdate(userId, userName);
        verify(redisClient, times(1)).saveOrUpdate(userId, userName);
    }
    @Test
    public void testSaveOrUpdate_Redis_Failure(){
        int userId = 222;
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        doThrow(new UnableToAddKeyException("Unable to add key")).when(redisClient).saveOrUpdate(userId,userName);
        assertThrows(UnableToAddKeyException.class,()->cacheDao.saveOrUpdate(userId,userName));
    }

    @Test
    public void testGetClient_ApacheIgnite(){
        cacheDao.cacheClient=AppConstants.CACHE_APACHE_IGNITE;
        GenericCacheClient result=cacheDao.getClient();
        assertEquals(apacheIgniteClient,result);
    }

    @Test
    public void testGetClient_Redis(){
        cacheDao.cacheClient=AppConstants.CACHE_REDIS;
        GenericCacheClient result=cacheDao.getClient();
        assertEquals(redisClient,result);
    }
}
