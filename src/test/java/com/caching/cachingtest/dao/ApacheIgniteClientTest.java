package com.caching.cachingtest.dao;

import com.caching.cachingtest.model.CacheMap;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("classpath:test.properties")
public class ApacheIgniteClientTest {
    @InjectMocks
    ApacheIgniteClient apacheIgniteClient;
    @Mock
    IgniteClient igniteClient;
    @Mock
    ClientCache<String, String> clientCache;
    @Test
    public void testGetUserById_Success() {
        String key="Test123";
        String expectedvalue="Test";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Cache");
        when(clientCache.get(key)).thenReturn(expectedvalue);
        String actualvalue=apacheIgniteClient.getValueById(key);
        Assert.assertEquals(expectedvalue, actualvalue);
    }

    @Test
    public void testGetUserById_Failure(){
        String key="Test123";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        when(igniteClient.getOrCreateCache("Cache")).thenThrow(new RuntimeException("Error while searching user with key from Apache Ignite"));
        String result=apacheIgniteClient.getValueById(key);
        Assert.assertNull(result);
    }

    @Test
    public void testDelete_Success(){
        String key="Test123";
        Boolean expectedStatus=true;
        when(clientCache.remove(key)).thenReturn(expectedStatus);
        Boolean actualStatus= apacheIgniteClient.delete(key);
        Assert.assertEquals(expectedStatus,actualStatus);
    }

    @Test
    public void testDelete_Failure(){
        String key="Test123";
        when(clientCache.remove(any())).thenThrow(new RuntimeException("Error while deleting key"));
        boolean result= apacheIgniteClient.delete(key);
        Assert.assertFalse(result);
    }
    @Test
    public void testSaveOrUpdate_Success(){
        String key="Test123";
        String expectedvalue="Test";
        ClientCache<String, String> clientCache = Mockito.mock(ClientCache.class);
        doReturn(clientCache).when(igniteClient).getOrCreateCache("Cache");
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        doNothing().when(clientCache).put(any(),any());
        apacheIgniteClient.saveOrUpdate(new CacheMap(key,expectedvalue,30L));
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
    @Test
    public void testSaveOrUpdate_Failure(){
        String key="Test123";
        String expectedvalue="Test";
        when(igniteClient.getOrCreateCache("Cache")).thenThrow(new RuntimeException("Error while saving/updating the value for key"));
        Assert.assertThrows(RuntimeException.class,()->apacheIgniteClient.saveOrUpdate(new CacheMap(key,expectedvalue,30L)));
    }
}
