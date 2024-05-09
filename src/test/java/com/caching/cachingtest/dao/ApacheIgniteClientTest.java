package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;
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
    @Value("${cache.name:Cache}")
    String cacheName;

    @Test
    public void testGetUserById_Success() {
        String key="Test123";
        String expectedvalue="Test";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(cacheName);
        when(clientCache.get(key)).thenReturn(expectedvalue);
        String actualvalue=apacheIgniteClient.getValueById(key);
        assertEquals(expectedvalue, actualvalue);
    }

    @Test
    public void testGetUserById_Failure(){
        String key="Test123";
        when(igniteClient.getOrCreateCache(cacheName)).thenThrow(new RuntimeException("Error while searching user with key from Apache Ignite"));
        String result=apacheIgniteClient.getValueById(key);
        Assert.assertNull(result);
    }

    @Test
    public void testDelete_Success(){
        String key="Test123";
        Boolean expectedStatus=true;
        doReturn(clientCache).when(igniteClient).getOrCreateCache(cacheName);
        when(clientCache.remove(key)).thenReturn(expectedStatus);
        Boolean actualStatus= apacheIgniteClient.delete(key);
        assertEquals(expectedStatus,actualStatus);
    }

    @Test
    public void testDelete_Failure(){
        String key="Test123";
        when(igniteClient.getOrCreateCache(cacheName)).thenThrow(new RuntimeException("Error while deleting key"));
        Boolean expectedStatus = clientCache.remove(key);
        Boolean result= apacheIgniteClient.delete(key);
        assertEquals(expectedStatus,result);
        Assert.assertFalse(result);
    }
    @Test
    public void testSaveOrUpdate_Success_SYNC(){
        String key="Test123";
        String expectedvalue="Test";
        apacheIgniteClient.cacheRebalanceMode="SYNC";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        doNothing().when(clientCache).put(any(),any());
        apacheIgniteClient.save(new CacheMap(key,expectedvalue,30L));
        verify(clientCache,times(1)).put(key,expectedvalue);
    }

    @Test
    public void testSaveOrUpdate_Success_ASYNC(){
        String key="Test123";
        String expectedvalue="Test";
        apacheIgniteClient.cacheRebalanceMode="ASYNC";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        doNothing().when(clientCache).put(any(),any());
        apacheIgniteClient.save(new CacheMap(key,expectedvalue,30L));
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
    @Test
    public void testSaveOrUpdate_Success_NONE(){
        String key="Test123";
        String expectedvalue="Test";
        apacheIgniteClient.cacheRebalanceMode="NONE";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        doNothing().when(clientCache).put(any(),any());
        apacheIgniteClient.save(new CacheMap(key,expectedvalue,30L));
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
    @Test
    public void testSaveOrUpdate_Failure(){
        CacheMap cacheMap = new CacheMap("Test123", "Test", 30l);
        apacheIgniteClient.cacheRebalanceMode="SYNC";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        when(clientCache.get(cacheMap.getKey())).thenReturn(null);
        doThrow(new RuntimeException("Error while saving/updating the value for key")).when(clientCache).put(cacheMap.getKey(),cacheMap.getValue());
        Assert.assertThrows(RuntimeException.class, () -> apacheIgniteClient.save(cacheMap));
    }
    @Test
    public void testSaveOrUpdate_KeyExistsException(){
        CacheMap existingCacheMap = new CacheMap("existingKey", "existingValue", 30l);
        apacheIgniteClient.cacheRebalanceMode="SYNC";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        when(clientCache.get(existingCacheMap.getKey())).thenReturn("existingValue");
        Assert.assertThrows(KeyExistsException.class, () -> apacheIgniteClient.save(existingCacheMap));
    }
    @Test
    public void testGetCacheRebalanceingModeFromConfigSync(){
        apacheIgniteClient.cacheRebalanceMode="SYNC";
        CacheRebalanceMode result=apacheIgniteClient.getCacheRebalanceingModeFromConfig();
        assertEquals(CacheRebalanceMode.SYNC, result);
}
    @Test
    public void testGetCacheRebalanceingModeFromConfigAsync(){
        apacheIgniteClient.cacheRebalanceMode = "ASYNC";
        CacheRebalanceMode result=apacheIgniteClient.getCacheRebalanceingModeFromConfig();
        assertEquals(CacheRebalanceMode.ASYNC, result);
    }
    @Test
    public void testGetCacheRebalanceingModeFromConfigNone(){
        apacheIgniteClient.cacheRebalanceMode="NONE";
        CacheRebalanceMode result=apacheIgniteClient.getCacheRebalanceingModeFromConfig();
        assertEquals(CacheRebalanceMode.NONE, result);
    }
    @Test
    public void testGetCacheRebalanceingModeFromConfigDefault(){
        apacheIgniteClient.cacheRebalanceMode="default";
        CacheRebalanceMode result=apacheIgniteClient.getCacheRebalanceingModeFromConfig();
        assertEquals(CacheRebalanceMode.SYNC, result);
    }
    @Test
    public void testToString(){
        String result=apacheIgniteClient.toString();
        assertEquals("ApacheIgniteClient",result);
    }
}
