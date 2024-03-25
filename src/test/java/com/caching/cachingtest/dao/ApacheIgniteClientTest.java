package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
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
        Assert.assertEquals(expectedvalue, actualvalue);
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
        Assert.assertEquals(expectedStatus,actualStatus);
    }

    @Test
    public void testDelete_Failure(){
        String key="Test123";
        when(igniteClient.getOrCreateCache(cacheName)).thenThrow(new RuntimeException("Error while deleting key"));
        Boolean expectedStatus = clientCache.remove(key);
        Boolean result= apacheIgniteClient.delete(key);
        Assert.assertEquals(expectedStatus,result);
        Assert.assertFalse(result);
    }
    @Test
    public void testSaveOrUpdate_Success(){
        String key="Test123";
        String expectedvalue="Test";
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        doNothing().when(clientCache).put(any(),any());
        apacheIgniteClient.saveOrUpdate(new CacheMap(key,expectedvalue,30L));
        verify(clientCache,times(1)).put(key,expectedvalue);
    }
    @Test
    public void testSaveOrUpdate_Failure(){
        CacheMap cacheMap = new CacheMap("Test123", "Test", 30l);
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        when(clientCache.get(cacheMap.getKey())).thenReturn(null);
        doThrow(new RuntimeException("Error while saving/updating the value for key")).when(clientCache).put(cacheMap.getKey(),cacheMap.getValue());
        Assert.assertThrows(RuntimeException.class, () -> apacheIgniteClient.saveOrUpdate(cacheMap));
    }
    @Test
    public void testSaveOrUpdate_KeyExistsException(){
        CacheMap existingCacheMap = new CacheMap("existingKey", "existingValue", 30l);
        doReturn(clientCache).when(igniteClient).getOrCreateCache(any(ClientCacheConfiguration.class));
        doReturn(clientCache).when(clientCache).withExpirePolicy(any());
        when(clientCache.get(existingCacheMap.getKey())).thenReturn("existingValue");
        Assert.assertThrows(KeyExistsException.class, () -> apacheIgniteClient.saveOrUpdate(existingCacheMap));
    }

}
