package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.ApacheIgniteClient;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.dao.RedisClient;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
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
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

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
    @Mock
    public ApacheIgniteClient apacheIgniteClient;

    @Mock
    public HttpServletRequest httpServletRequest;

    @Test
    public void testApiIgnite_Success() {
        Mockito.when(cacheDao.getClient()).thenReturn(apacheIgniteClient);
        ResponseEntity<Response> response = cacheController.testApi(httpServletRequest);
        assertEquals("Cache Client :: apacheIgniteClient  -   RebalancingMode :: null", response.getBody().getMessage());
    }
    @Test
    public void testApiIgnite_Failure(){
        when(cacheDao.getClient()).thenThrow(new RuntimeException("Error while fetching the Client"));
        ResponseEntity<Response> response= cacheController.testApi(httpServletRequest);
        Assert.assertEquals("Internal Server Error",response.getBody().getStatus());
        Assert.assertEquals("Error occurred : Error while fetching the Client",response.getBody().getMessage());
    }
    @Test
    public void testApiRedis_Success() {
        Mockito.when(cacheDao.getClient()).thenReturn(new RedisClient());
        ResponseEntity<Response> response = cacheController.testApi(httpServletRequest);
        assertEquals("Cache Client :: RedisClient", response.getBody().getMessage());
    }
    @Test
    public void testApiRedis_Failure() {
        Mockito.when(cacheDao.getClient()).thenThrow(new RuntimeException("Error while fetching the Client"));
        ResponseEntity<Response> response = cacheController.testApi(httpServletRequest);
        Assert.assertEquals("Internal Server Error",response.getBody().getStatus());
        Assert.assertEquals("Error occurred : Error while fetching the Client",response.getBody().getMessage());
    }

    @Test
    public void testGetKey_Success() {
        String key = "123";
        String expectedValue = "Test";
        Mockito.when(userServiceImpl.getValueByKey(key)).thenReturn(expectedValue);
        ResponseEntity<Response> response = cacheController.getKey(httpServletRequest,key);
        Assert.assertEquals(AppConstants.SUCCESS, response.getBody().getStatus());
        assertEquals(expectedValue, response.getBody().getValue());
        assertEquals(key,response.getBody().getKey());
    }
    @Test
    public void testGetKey_Failure() {
        String key = "456";
        when(userServiceImpl.getValueByKey(key)).thenThrow(new RuntimeException("Key not found"));
        ResponseEntity<Response> response = cacheController.getKey(httpServletRequest,key);
        Assert.assertEquals("Not Found",response.getBody().getStatus());
        Assert.assertEquals("Error occurred : Key not found",response.getBody().getMessage());
        Assert.assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void testDelete_Success() {
        String validKey = "123";
        doNothing().when(userServiceImpl).delete(validKey);
        ResponseEntity<Response> responseEntity = cacheController.deleteKey(httpServletRequest,validKey);
        Assert.assertEquals("key 123 removed",responseEntity.getBody().getMessage());
    }

    @Test
    public void testDelete_Failure(){
        String invalidKey = "-1";
        String errorMessage="key not found";
        doThrow(new RuntimeException(errorMessage)).when(userServiceImpl).delete(invalidKey);
        ResponseEntity<Response> responseEntity = cacheController.deleteKey(httpServletRequest,invalidKey);
        assertEquals("Error occurred : "+errorMessage,responseEntity.getBody().getMessage());
        Assert.assertEquals("Not Found",responseEntity.getBody().getStatus());
        Assert.assertEquals(404,responseEntity.getStatusCodeValue());
    }
    @Test
    public void testAddKey_Success()  {
        CacheMap cacheMap = new CacheMap();
        cacheMap.setKey("01");
        cacheMap.setValue("data");
        cacheMap.setTtl(10L);
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("key " + cacheMap.getKey() + " added");
        doNothing().when(userServiceImpl).save(cacheMap);
        ResponseEntity<Response> response = cacheController.addKey(httpServletRequest,cacheMap);
        assertEquals(expectedResponse.getStatus(), response.getBody().getStatus());
        assertEquals(expectedResponse.getMessage(), response.getBody().getMessage());
    }

    @Test
    public void testAddKey_Success_TtlNull()  {
        CacheMap cacheMap = new CacheMap();
        cacheMap.setKey("01");
        cacheMap.setValue("data");
        cacheMap.setTtl(null);
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("key " + cacheMap.getKey() + " added");
        doNothing().when(userServiceImpl).save(cacheMap);
        ResponseEntity<Response> response = cacheController.addKey(httpServletRequest,cacheMap);
        assertEquals(expectedResponse.getStatus(), response.getBody().getStatus());
        assertEquals(expectedResponse.getMessage(), response.getBody().getMessage());
    }
    @Test
    public void testAddKey_Failure(){
        String invalidKey = "-1";
        String invalidValue = "invalidValue";
        String errorMessage = "Unable to save key";
        Mockito.doThrow(new RuntimeException(errorMessage)).when(userServiceImpl).save(new CacheMap(invalidKey, invalidValue,30L));
        ResponseEntity<Response> actualResponse = cacheController.addKey(httpServletRequest,new CacheMap(invalidKey, invalidValue,30L));
        assertEquals("Error occurred : " + errorMessage, actualResponse.getBody().getMessage());
        Assert.assertEquals(500,actualResponse.getStatusCodeValue());
    }
    @Test
    public void testAddKey_Failure_KeyExistsException(){
        String existingKey = "Test12";
        String existingValue = "existingValue";
        String errorMessage = "key already existing in cache";
        Mockito.doThrow(new KeyExistsException(errorMessage)).when(userServiceImpl).save(new CacheMap(existingKey, existingValue,30L));
        ResponseEntity<Response> actualResponse = cacheController.addKey(httpServletRequest,new CacheMap(existingKey, existingValue,30L));
        assertEquals(errorMessage, actualResponse.getBody().getMessage());
        Assert.assertEquals(400,actualResponse.getStatusCodeValue());
    }
    @Test
    public void testAddKey_Failure_InvalidTTl(){
        String existingKey = "Test12";
        String existingValue = "existingValue";
        String errorMessage = "key invalid ttl";
        Mockito.doThrow(new InvalidTTLException(errorMessage)).when(userServiceImpl).save(new CacheMap(existingKey, existingValue,-1l));
        ResponseEntity<Response> actualResponse = cacheController.addKey(httpServletRequest,new CacheMap(existingKey, existingValue,-1l));
        assertEquals(errorMessage, actualResponse.getBody().getMessage());
        Assert.assertEquals(400,actualResponse.getStatusCodeValue());
    }
}
