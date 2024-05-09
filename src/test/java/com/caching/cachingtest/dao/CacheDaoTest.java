package com.caching.cachingtest.dao;


import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import com.caching.cachingtest.model.CacheMap;
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
        String userId = "Test123";
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
        String userId = "123";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.getValueById(userId)).thenThrow(new CacheNotFoundException("Cache not found"));
        Assert.assertThrows(CacheNotFoundException.class,()-> cacheDao.getUserById(userId));
    }

    @Test
    public void testGetUserById_Redis_Success() throws CacheNotFoundException {
        String userId = "test456";
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
        String userId = "test456";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.getValueById(userId)).thenThrow(new CacheNotFoundException("Cache not found"));
        Assert.assertThrows(CacheNotFoundException.class,()-> cacheDao.getUserById(userId));
    }

    @Test
    public void testDelete_ApacheIgnite_Success() {
        String userId = "test456";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, times(1)).delete(userId);
        verify(redisClient, never()).delete(userId);
    }

    @Test
    public void testDelete_ApacheIgnite_Failure(){
        String userId = "test456";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        when(apacheIgniteClient.delete(userId)).thenThrow(new CacheNotFoundException("Failed to delete key from cache with exception : "));
        Assert.assertThrows(CacheNotFoundException.class,()->cacheDao.delete(userId));
    }

    @Test
    public void testDelete_Redis_Success() {
        String userId = "test456";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.delete(userId)).thenReturn(true);
        boolean result = cacheDao.delete(userId);
        assertTrue(result);
        verify(apacheIgniteClient, never()).delete(userId);
        verify(redisClient, times(1)).delete(userId);
    }

    @Test
    public void testDelete_Redis_Failure(){
        String userId = "test456";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        when(redisClient.delete(userId)).thenThrow(new CacheNotFoundException("Failed to delete key from cache with exception : "));
        Assert.assertThrows(CacheNotFoundException.class,()->cacheDao.delete(userId));
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite_Success() {
        String userId = "test456";
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        cacheDao.save(new CacheMap(userId, userName,30L));
        verify(apacheIgniteClient, times(1)).save(new CacheMap(userId, userName,30L));
        verify(redisClient, never()).save(new CacheMap(userId, userName,30L));
    }

    @Test
    public void testSaveOrUpdate_ApacheIgnite_Failure(){
        String userId = "test456";
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        doThrow(new UnableToAddKeyException("Unable to add key")).when(apacheIgniteClient).save(new CacheMap(userId,userName,30L));
        assertThrows(UnableToAddKeyException.class,()->cacheDao.save(new CacheMap(userId,userName,30L)));
    }
    @Test
    public void testSaveOrUpdate_ApacheIgnite_KeyExistsException(){
        CacheMap existingCacheMap = new CacheMap("existingKey", "existingValue", 30l);
        cacheDao.cacheClient = AppConstants.CACHE_APACHE_IGNITE;
        doThrow(new KeyExistsException("key already existing in cache")).when(apacheIgniteClient).save(existingCacheMap);
        assertThrows(KeyExistsException.class,()->cacheDao.save(existingCacheMap));
    }

    @Test
    public void testSaveOrUpdate_Redis_Success() {
        String userId = "test456";
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        cacheDao.save(new CacheMap(userId, userName,30l));
        verify(apacheIgniteClient, never()).save(new CacheMap(userId, userName,30L));
        verify(redisClient, times(1)).save(new CacheMap(userId, userName,30L));
    }
    @Test
    public void testSaveOrUpdate_Redis_Failure(){
        String userId = "test456";
        String userName = "Test";
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        doThrow(new UnableToAddKeyException("Unable to add key")).when(redisClient).save(new CacheMap(userId,userName,30L));
        assertThrows(UnableToAddKeyException.class,()->cacheDao.save(new CacheMap(userId,userName,30L)));
    }

    @Test
    public void testSaveOrUpdate_Redis_KeyExistsException(){
        CacheMap existingCacheMap = new CacheMap("existingKey", "existingValue", 30l);
        cacheDao.cacheClient = AppConstants.CACHE_REDIS;
        doThrow(new KeyExistsException("key already existing in cache")).when(redisClient).save(existingCacheMap);
        assertThrows(KeyExistsException.class,()->cacheDao.save(existingCacheMap));
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
