package com.cacheing.cacheingtest.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheMap {
    private int key;
    private String value;

    @Override
    public String toString() {
        return "CacheMap{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}
