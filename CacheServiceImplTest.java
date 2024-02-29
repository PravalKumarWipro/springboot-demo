package com.cacheing.cacheingtest.service;

import com.cacheing.cacheingtest.dao.CacheDao;
import com.cacheing.cacheingtest.exception.CacheNotFoundException;
import com.cacheing.cacheingtest.exception.UnableToAddKeyException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {
    @InjectMocks
    CacheServiceImpl userServiceImpl;
    @Mock
    public CacheDao cacheDao;

    @Test
    public void testGetUserById_Success() throws CacheNotFoundException {
        int key = 123;
        String expectedUname = "Test";
        when(cacheDao.getUserById(key)).thenReturn(expectedUname);
        String actualUname = userServiceImpl.getValueByKey(key);
        assertEquals(expectedUname , actualUname);
    }
    @Test
    public void testGetUserById_Failure() {
        int key = 123;
        when(cacheDao.getUserById(key)).thenReturn(null);
        assertThrows(CacheNotFoundException.class, () -> userServiceImpl.getValueByKey(key));
    }
    @Test
    public void testDelete_Success() throws CacheNotFoundException {
        int key = 123;
        when(cacheDao.delete(key)).thenReturn(true);
        boolean status = cacheDao.delete(key);
        assertTrue(status);
    }
    @Test
    public void testDelete_Failure() {
        int key = 123;
        when(cacheDao.delete(key)).thenReturn(false);
        assertThrows(CacheNotFoundException.class, () -> userServiceImpl.delete(key));
    }
    @Test
    public void testSaveOrUpdate_Success() throws UnableToAddKeyException {
        int key = 123;
        String value = "Test";
        Mockito.doNothing().when(cacheDao).saveOrUpdate(key, value);
        userServiceImpl.saveOrUpdate(key, value);
        verify(cacheDao, times(1)).saveOrUpdate(key, value);
    }
    @Test
    public void testSaveOrUpdate_Failure() throws UnableToAddKeyException {
        int key = 123;
        String value = "Test";
        Mockito.doThrow(new RuntimeException()).when(cacheDao).saveOrUpdate(key, value);
        assertThrows(UnableToAddKeyException.class, () -> userServiceImpl.saveOrUpdate(key, value));
    }
}
