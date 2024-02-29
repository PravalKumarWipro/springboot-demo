package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.dao.ApacheIgniteClient;
import com.cacheing.cacheingtest.dao.CacheDao;
import com.cacheing.cacheingtest.dao.RedisClient;
import com.cacheing.cacheingtest.dao.UnleashCustomClient;
import com.cacheing.cacheingtest.model.CacheMap;
import com.cacheing.cacheingtest.model.Response;
import com.cacheing.cacheingtest.service.CacheServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerTest {
    @InjectMocks
    private CacheController cacheController;
    @Mock
    public CacheServiceImpl userServiceImpl;

    @Mock
    public CacheDao cacheDao;


    @Test
    public void testApiIgnite() {
        String token = "abc";
        Mockito.when(cacheDao.getClient()).thenReturn(new ApacheIgniteClient());
        Response response = cacheController.testApi();
        System.out.println("response :: " + response);
        assertEquals("Cache Client :: ApacheIgniteClient", response.getMessage());
    }

    @Test
    public void testApiRedis() {
        String token = "abc";
        Mockito.when(cacheDao.getClient()).thenReturn(new RedisClient());
        Response response = cacheController.testApi();
        System.out.println("response :: " + response);
        assertEquals("Cache Client :: RedisClient", response.getMessage());
    }

    @Test
    public void testGetKey_KeyFound() {
        Integer key = 123;
        String expectedValue = "Abc";
        Mockito.when(userServiceImpl.getValueByKey(key)).thenReturn(expectedValue);
        Response response = cacheController.getKey(key);
        assertEquals(AppConstants.SUCCESS, response.getStatus());
        assertEquals(expectedValue, response.getValue());
        assertEquals(key,response.getKey());
    }

    @Test
    public void testDelete_key() {
        int key = 123;
        cacheController.deleteKey(key);
        verify(userServiceImpl).delete(key);
    }

    @Test
    public void testAddKey_Success()  {
        int key1 = 123;
        CacheMap cacheMap = new CacheMap(key1, "Test");
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("key " + cacheMap.getKey() + " added");
        doNothing().when(userServiceImpl).saveOrUpdate(cacheMap.getKey(),cacheMap.getValue());
        Response response = cacheController.addKey(cacheMap);
        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
    @Test
    public void testSetUnleashToken_Success() {
        String token = "valid-token";
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("Token Updated");
        Response response = cacheController.setUnleashToken(token);
        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
    @Test
    public void testSetUnleashToken_EmptyToken() {
        String token = "";
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("Invalid Token");
        Response response = cacheController.setUnleashToken(token);
        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
    @Test
    public void testGetUnleashToken() {
        String token = "valid-token";
        UnleashCustomClient.token = token;
        Response expectedResponse = new Response(AppConstants.SUCCESS);
        expectedResponse.setMessage("token : " + token);
        Response response = cacheController.getUnleashToken();
        assertEquals(expectedResponse.getStatus(), response.getStatus());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
