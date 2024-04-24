package com.foodshare.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setKeyValueWithExpire(String key, String value, long timeout, TimeUnit unit) {
        try {
            ValueOperations<String, String> values = redisTemplate.opsForValue();
            values.set(key, value, timeout, unit);
        } catch (Exception e) {
            System.err.println("Error saving data in Redis: " + e.getMessage());
        }
    }


    public void saveToken(String username, String refreshToken, long duration) {
        String key = "REFRESH_TOKEN:" + username;
        System.out.println("Saving token to Redis with key: " + key + " and duration: " + duration + " seconds.");
        redisTemplate.opsForValue().set(key, refreshToken, duration, TimeUnit.MILLISECONDS);
        System.out.println("Token saved successfully.");
    }
}
