package com.example.system_design_java_project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    @Autowired
    private RedisService redisService;

    @Value("${app.rate-limit.max-requests:5}")
    private long maxRequests;

    @Value("${app.rate-limit.window-minutes:1}")
    private long windowMinutes;

    public boolean isAllowed(String clientIdentifier){
        String key = "rate-limit:" + clientIdentifier;
        long currentCount = redisService.incrementWithExpiry(key, windowMinutes, TimeUnit.MINUTES);

        return currentCount <= maxRequests;
    }

    public long getRemainingRequests(String clientIdentifier){
        String key = "rate-limit:" + clientIdentifier;
        String count = redisService.get(key);

        if(count == null)
            return maxRequests;

        return Math.max(0, maxRequests - Long.parseLong(count));
    }

    public long getResetTime(String clientIdentifier){
        String key = "rate-limit:" + clientIdentifier;
        long ttl = redisService.getExpiry(key, TimeUnit.SECONDS);

        return ttl < 0 ? 0 : ttl;
    }
}
