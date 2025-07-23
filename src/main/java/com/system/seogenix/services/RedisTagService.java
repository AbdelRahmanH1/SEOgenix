package com.system.seogenix.services;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RedisTagService {
    public final StringRedisTemplate stringRedisTemplate;
    private static final String TAG_PREFIX = "tag:";

    public List<String> getAllTags(String title) {
        String key = TAG_PREFIX + title.toLowerCase().trim();
        var members = stringRedisTemplate.opsForSet().members(key);
        return members!=null?members.stream().toList():null;
    }

    public List<String> getRandomTags(String title, int count) {
        String key = TAG_PREFIX + title.toLowerCase().trim();
        var members = stringRedisTemplate.opsForSet().members(key);
        if (members == null || members.isEmpty()) return null;
        List<String> tags = new ArrayList<>(members);
        Collections.shuffle(tags);
        return tags.stream().limit(count).collect(Collectors.toList());
    }

    public void saveTag(String title, List<String> tags) {
        String key = TAG_PREFIX + title.toLowerCase().trim();
        stringRedisTemplate.delete(key);
        stringRedisTemplate.opsForSet().add(key, tags.toArray(new String[0]));
        stringRedisTemplate.expire(key, Duration.ofHours(1));
    }

}
