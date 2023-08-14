package com.boardgaming.domain.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class GomokuRoomIdGenerator {
    private final ValueOperations<String, Long> valueOperations;
    private final String key = "gomoku-room-id-generator";

    public GomokuRoomIdGenerator(RedisTemplate<String, Long> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public Long generateNextRoomId() {
        return valueOperations.increment(key);
    }
}
