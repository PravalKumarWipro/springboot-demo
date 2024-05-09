package com.caching.cachingtest.service;

import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import com.caching.cachingtest.model.CacheMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {
    @InjectMocks
    CacheServiceImpl userServiceImpl;
    @Mock
    public CacheDao cacheDao;

    @Test
    public void testGetUserById_Success() throws CacheNotFoundException {
        String key = "Test123";
        String expectedUname = "Test";
        when(cacheDao.getUserById(key)).thenReturn(expectedUname);
        String actualUname = userServiceImpl.getValueByKey(key);
        assertEquals(expectedUname , actualUname);
    }
    @Test
    public void testGetUserById_Failure_Null() {
        String key = "Test123";
        when(cacheDao.getUserById(key)).thenReturn(null);
        assertThrows(CacheNotFoundException.class, () -> userServiceImpl.getValueByKey(key));
    }

    @Test
    public void testGetUserById_Failure_Empty() {
        String key = "Test123";
        when(cacheDao.getUserById(key)).thenReturn("");
        assertThrows(CacheNotFoundException.class, () -> userServiceImpl.getValueByKey(key));
    }
    @Test
    public void testGetUserById_Exception(){
        String invalidKey= "-1";
        when(cacheDao.getUserById(invalidKey)).thenThrow(new CacheNotFoundException("Exception"));
        assertThrows(CacheNotFoundException.class,()->userServiceImpl.getValueByKey(invalidKey));
    }
    @Test
    public void testDelete_Success() throws CacheNotFoundException {
        String key = "Test123";
        when(cacheDao.delete(key)).thenReturn(true);
        boolean status = cacheDao.delete(key);
        assertDoesNotThrow(()->userServiceImpl.delete(key));
        assertTrue(status);
    }

    @Test
    public void testDelete_Failure() {
        String key = "Test123";
        when(cacheDao.delete(key)).thenReturn(false);
        assertThrows(CacheNotFoundException.class, () -> userServiceImpl.delete(key));
    }
    @Test
    public void testSaveOrUpdate_Success() throws UnableToAddKeyException {
        String key = "Test123";
        String value = "Test";
        Mockito.doNothing().when(cacheDao).save(new CacheMap(key, value,30L));
        userServiceImpl.save(new CacheMap(key, value,30L));
        verify(cacheDao, times(1)).save(new CacheMap(key, value,30L));
    }
    @Test
    public void testSaveOrUpdate_Failure() throws UnableToAddKeyException {
        String key = "Test123";
        String value = "Test";
        Mockito.doThrow(new RuntimeException()).when(cacheDao).save(new CacheMap(key, value,30L));
        assertThrows(UnableToAddKeyException.class, () -> userServiceImpl.save(new CacheMap(key, value,30L)));
    }
    @Test(expected = InvalidTTLException.class)
    public void testSaveOrUpdate_Failure_NegativeTtl() {
        String key = "Test123";
        String value = "Test";
        long ttl = -1l;
        userServiceImpl.save(new CacheMap(key,value,ttl));
    }
    @Test(expected = InvalidTTLException.class)
    public void testSaveOrUpdate_Failure_ZeroTtl() {
        String key = "Test123";
        String value = "Test";
        long ttl = 0l;
        userServiceImpl.save(new CacheMap(key,value,ttl));
    }
    @Test
    public void testSaveOrUpdate_keyExistsException() {
        String key = "existingKey";
        String value = "existingValue";
        Mockito.doThrow(new KeyExistsException()).when(cacheDao).save(new CacheMap(key, value,30L));
        assertThrows(KeyExistsException.class, () -> userServiceImpl.save(new CacheMap(key, value,30L)));
    }
}
