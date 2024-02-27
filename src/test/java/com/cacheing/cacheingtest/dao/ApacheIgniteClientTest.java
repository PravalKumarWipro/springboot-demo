package com.cacheing.cacheingtest.dao;

import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApacheIgniteClientTest {
    @InjectMocks
    ApacheIgniteClient apacheIgniteClient;
    @Mock
    IgniteClient igniteClient;
    private static final String CACHE_NAME = "Users";

    @Test
    public void testGetUserById() {
        int key=123;
        String expectedvalue="Test";
        ClientCache<Integer, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        when(clientCache.get(key)).thenReturn(expectedvalue);
        String actualvalue=apacheIgniteClient.getValueById(key);
        Assert.assertEquals(expectedvalue, actualvalue);
    }

    @Test
    public void testDelete(){
        int key=123;
        Boolean expectedStatus=true;
        ClientCache<Integer, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        when(clientCache.remove(key)).thenReturn(expectedStatus);
        Boolean actualStatus= apacheIgniteClient.delete(key);
        Assert.assertEquals(expectedStatus,actualStatus);
    }
    @Test
    public void testSaveOrUpdate(){
        int key=123;
        String expectedvalue="Test";
        ClientCache<Integer, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        apacheIgniteClient.saveOrUpdate(key,expectedvalue);
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
}
