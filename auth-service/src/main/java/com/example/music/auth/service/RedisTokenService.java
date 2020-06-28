package com.example.music.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


@Component
public class RedisTokenService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public String getVerificationCode(String phone,String area,String type) {
        String keyName = String.format("%s:%s:%s",phone,area,type);
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        return stringStringValueOperations.get(keyName);
    }

    public void removeVerificationCode(String phone,String area,String type) {
        String keyName = String.format("%s:%s:%s",phone,area,type);
        redisTemplate.delete(keyName);
    }
}
