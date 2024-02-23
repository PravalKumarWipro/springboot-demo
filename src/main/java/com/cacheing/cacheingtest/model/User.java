package com.cacheing.cacheingtest.model;


import org.springframework.stereotype.Component;

@Component
public class User {
    private Integer key;
    private String value;

    public User() {
    }

    public User(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + key + '\'' +
                ", userName='" + value + '\'' +
                '}';
    }
}
