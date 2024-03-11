package com.caching.cachingtest.dao;

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

    @Test
    public void testGetUserById_Success() {
        String key="Test123";
        String expectedvalue="Test";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        when(clientCache.get(key)).thenReturn(expectedvalue);
        String actualvalue=apacheIgniteClient.getValueById(key);
        Assert.assertEquals(expectedvalue, actualvalue);
    }

    @Test
    public void testGetUserById_Failure(){
        String key="Test123";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        when(igniteClient.getOrCreateCache("Users")).thenThrow(new RuntimeException("Error while searching user with key from Apache Ignite"));
        String result=apacheIgniteClient.getValueById(key);
        Assert.assertNull(result);
    }

    @Test
    public void testDelete_Success(){
        String key="Test123";
        Boolean expectedStatus=true;
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        when(clientCache.remove(key)).thenReturn(expectedStatus);
        Boolean actualStatus= apacheIgniteClient.delete(key);
        Assert.assertEquals(expectedStatus,actualStatus);
    }

    @Test
    public void testDelete_Failure(){
        String key="Test123";
        when(igniteClient.getOrCreateCache("Users")).thenThrow(new RuntimeException("Error while deleting key"));
        boolean result= apacheIgniteClient.delete(key);
        Assert.assertFalse(result);
    }
    @Test
    public void testSaveOrUpdate_Success(){
        String key="Test123";
        String expectedvalue="Test";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Users");
        apacheIgniteClient.saveOrUpdate(key,expectedvalue);
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
    @Test
    public void testSaveOrUpdate_Failure(){
        String key="Test123";
        String expectedvalue="Test";
        when(igniteClient.getOrCreateCache("Users")).thenThrow(new RuntimeException("Error while saving/updating the value for key"));
        Assert.assertThrows(RuntimeException.class,()->apacheIgniteClient.saveOrUpdate(key,expectedvalue));
    }
}
