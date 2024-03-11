package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.ApacheIgniteClient;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.dao.RedisClient;
import com.caching.cachingtest.model.CacheMap;
import com.caching.cachingtest.model.Response;
import com.caching.cachingtest.service.CacheServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerTest {
    @InjectMocks
    private CacheController cacheController;
    @Mock
    public CacheServiceImpl userServiceImpl;

    @Mock
    public CacheDao cacheDao;


    @Test
    public void testApiIgnite_Success() {
        Mockito.when(cacheDao.getClient()).thenReturn(new ApacheIgniteClient());
        Response response = cacheController.testApi();
        assertEquals("Cache Client :: ApacheIgniteClient", response.getMessage());
    }
    @Test
    public void testApiIgnite_Failure(){
        when(cacheDao.getClient()).thenThrow(new RuntimeException("Error while fetching the Client"));
        Response response= cacheController.testApi();
        Assert.assertEquals(AppConstants.SUCCESS,response.getStatus());
        Assert.assertEquals("Error occurred : Error while fetching the Client",response.getMessage());
    }
    @Test
    public void testApiRedis_Success() {
        Mockito.when(cacheDao.getClient()).thenReturn(new RedisClient());
        Response response = cacheController.testApi();
        assertEquals("Cache Client :: RedisClient", response.getMessage());
    }
    @Test
    public void testApiRedis_Failure() {
        Mockito.when(cacheDao.getClient()).thenThrow(new RuntimeException("Error while fetching the Client"));
        Response response = cacheController.testApi();
        Assert.assertEquals(AppConstants.SUCCESS,response.getStatus());
        Assert.assertEquals("Error occurred : Error while fetching the Client",response.getMessage());
    }

    @Test
    public void testGetKey_Success() {
        String key = "123";
        String expectedValue = "Test";
        Mockito.when(userServiceImpl.getValueByKey(key)).thenReturn(expectedValue);
        Response response = cacheController.getKey(key);
        Assert.assertEquals(AppConstants.SUCCESS, response.getStatus());
        assertEquals(expectedValue, response.getValue());
        assertEquals(key,response.getKey());
    }
    @Test
    public void testGetKey_Failure() {
        String key = "456";
        when(userServiceImpl.getValueByKey(key)).thenThrow(new RuntimeException("Key not found"));
        Response response = cacheController.getKey(key);
        Assert.assertEquals(AppConstants.SUCCESS,response.getStatus());
        Assert.assertEquals("Error occurred : Key not found",response.getMessage());
    }

    @Test
    public void testDelete_Success() {
        String validKey = "123";
        doNothing().when(userServiceImpl).delete(validKey);
        Response responseEntity = cacheController.deleteKey(validKey);
        Assert.assertEquals("key 123 removed",responseEntity.getMessage());
    }

    @Test
    public void testDelete_Failure(){
        String invalidKey = "-1";
        String errorMessage="key not found";
        doThrow(new RuntimeException(errorMessage)).when(userServiceImpl).delete(invalidKey);
        Response responseEntity = cacheController.deleteKey(invalidKey);
        assertEquals("Error occurred : "+errorMessage,responseEntity.getMessage());
    }

    @Test
    public void testAddKey_Success()  {
        String key1 = "Test123";
        CacheMap cacheMap = new CacheMap(key1, "Test");
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("key " + cacheMap.getKey() + " added");
        doNothing().when(userServiceImpl).saveOrUpdate(cacheMap.getKey(),cacheMap.getValue());
        Response response = cacheController.addKey(cacheMap);
        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
    @Test
    public void testAddKey_Failure(){
        String invalidKey = "-1";
        String invalidValue = "invalidValue";
        String errorMessage = "Unable to save key";
        Mockito.doThrow(new RuntimeException(errorMessage)).when(userServiceImpl).saveOrUpdate(invalidKey, invalidValue);
        Response actualResponse = cacheController.addKey(new CacheMap(invalidKey, invalidValue));
        assertEquals("Error occurred : " + errorMessage, actualResponse.getMessage());
    }
}
