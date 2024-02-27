package com.cacheing.cacheingtest.controller;

import com.cacheing.cacheingtest.dao.ApacheIgniteClient;
import com.cacheing.cacheingtest.dao.CacheDao;
import com.cacheing.cacheingtest.dao.RedisClient;
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

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerTest {
    @InjectMocks
    CacheController cacheController;
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
        Assert.assertEquals("Hello From SpringBoot!!! Cache Client we are using  :: ApacheIgniteClient", response);
    }

    @Test
    public void testApiRedis() {
        String token = "abc";
        Mockito.when(cacheDao.getClient()).thenReturn(new RedisClient());
        Response response = cacheController.testApi();
        System.out.println("response :: " + response);
        Assert.assertEquals("Hello From SpringBoot!!! Cache Client we are using  :: RedisClient", response);
    }

    @Test
    public void testGetBooks() {
        CacheController controller = new CacheController();
        CacheServiceImpl mockUserService = Mockito.mock(CacheServiceImpl.class);
        controller.userServiceImpl = mockUserService;
        int key = 1234;
        String expectedUser = "ABC";
        String token = "abc";
        Mockito.when(mockUserService.getValueByKey(key)).thenReturn(expectedUser);
        Response response = controller.getKey(key);
        Assert.assertEquals(expectedUser, response);
    }

    @Test
    public void testDeleteBook() {
        CacheController controller = new CacheController();
        CacheServiceImpl mockUserService = Mockito.mock(CacheServiceImpl.class);
        controller.userServiceImpl = mockUserService;
        int key = 123;
        String token = "abc";
        controller.deleteKey(key);
        Mockito.verify(mockUserService).delete(key);
    }

    @Test
    public void testAddUser() {
        CacheController controller = new CacheController();
        CacheServiceImpl mockUserService = Mockito.mock(CacheServiceImpl.class);
        controller.userServiceImpl = mockUserService;
        CacheMap cacheMap = new CacheMap(234, "xyz");
        String expectedResponse = "User with key 234 Added";
        String token = "abc";
        Response response = controller.addKey(cacheMap);
        Mockito.verify(mockUserService).saveOrUpdate(cacheMap.getKey(), cacheMap.getValue());
        Assert.assertEquals(expectedResponse, response);

    }
}
