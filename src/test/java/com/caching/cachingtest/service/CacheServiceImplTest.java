package com.caching.cachingtest.service;

import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.*;
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
    public void testSave_Success() throws UnableToAddKeyException {
        String key = "Test123";
        String value = "Test";
        Mockito.doNothing().when(cacheDao).save(new CacheMap(key, value,30L));
        userServiceImpl.save(new CacheMap(key, value,30L));
        verify(cacheDao, times(1)).save(new CacheMap(key, value,30L));
    }
    @Test
    public void testSave_Failure() throws UnableToAddKeyException {
        String key = "Test123";
        String value = "Test";
        Mockito.doThrow(new RuntimeException()).when(cacheDao).save(new CacheMap(key, value,30L));
        assertThrows(UnableToAddKeyException.class, () -> userServiceImpl.save(new CacheMap(key, value,30L)));
    }
    @Test(expected = InvalidTTLException.class)
    public void testSave_Failure_NegativeTtl() {
        String key = "Test123";
        String value = "Test";
        long ttl = -1l;
        userServiceImpl.save(new CacheMap(key,value,ttl));
    }
    @Test(expected = InvalidTTLException.class)
    public void testSave_Failure_ZeroTtl() {
        String key = "Test123";
        String value = "Test";
        long ttl = 0l;
        userServiceImpl.save(new CacheMap(key,value,ttl));
    }
    @Test
    public void testSave_keyExistsException() {
        String key = "existingKey";
        String value = "existingValue";
        Mockito.doThrow(new KeyExistsException()).when(cacheDao).save(new CacheMap(key, value,30L));
        assertThrows(KeyExistsException.class, () -> userServiceImpl.save(new CacheMap(key, value,30L)));
    }
    @Test
    public void testUpdate_Success() throws UnableToAddKeyException {
        String key = "Test123";
        String updatedvalue = "Test";
        Mockito.doNothing().when(cacheDao).update(new CacheMap(key, updatedvalue,30L));
        userServiceImpl.update(new CacheMap(key, updatedvalue,30L));
        verify(cacheDao, times(1)).update(new CacheMap(key, updatedvalue,30L));
    }
    @Test
    public void testUpdate_Failure() throws UnableToUpdateKeyException {
        String key = "Test123";
        String updatedvalue = "Test";
        Mockito.doThrow(new RuntimeException()).when(cacheDao).update(new CacheMap(key, updatedvalue,30L));
        assertThrows(UnableToUpdateKeyException.class, () -> userServiceImpl.update(new CacheMap(key, updatedvalue,30L)));
    }
    @Test(expected = InvalidTTLException.class)
    public void testUpdate_Failure_NegativeTtl() {
        String key = "Test123";
        String updatedvalue = "Test";
        long ttl = -1l;
        userServiceImpl.update(new CacheMap(key,updatedvalue,ttl));
    }
    @Test(expected = InvalidTTLException.class)
    public void testUpdate_Failure_ZeroTtl() {
        String key = "Test123";
        String updatedvalue = "Test";
        long ttl = 0l;
        userServiceImpl.update(new CacheMap(key,updatedvalue,ttl));
    }
    @Test
    public void testUpdate_keyNotExistsException() {
        String key = "123";
        String updatedvalue = "Test";
        Mockito.doThrow(new KeyNotExistsException()).when(cacheDao).update(new CacheMap(key, updatedvalue,30L));
        assertThrows(KeyNotExistsException.class, () -> userServiceImpl.update(new CacheMap(key, updatedvalue,30L)));
    }
}
