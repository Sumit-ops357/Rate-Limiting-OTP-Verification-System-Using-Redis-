package com.example.system_design_java_project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void setWithExpiry(String key, String value, long timeout, TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    public long incrementWithExpiry(String key,long timeout,TimeUnit unit){
        long count = redisTemplate.opsForValue().increment(key);

        if(count==1){
            redisTemplate.expire(key, timeout, unit);
        }

        return count;
    }

    public Boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public Boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

    public long getExpiry(String key, TimeUnit unit){

        return redisTemplate.getExpire(key, unit);
    }
}
