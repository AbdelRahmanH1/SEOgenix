package com.system.seogenix.services;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@AllArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate redis;
    private static final int LIMIT = 5;
    private static final Duration TTL = Duration.ofMinutes(1);

    public boolean isRateLimited(String ip) {
        String key = "rate:" + ip.replace(":", "");

        Long count = redis.opsForValue().increment(key);

        if (count == null) return true;
        if (count == 1) {
            redis.expire(key, TTL);
        }

        return count > LIMIT;
    }

}

