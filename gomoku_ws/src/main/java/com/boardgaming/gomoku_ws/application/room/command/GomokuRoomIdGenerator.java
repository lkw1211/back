package com.boardgaming.gomoku_ws.application.room.command;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class GomokuRoomIdGenerator {
    private final RedisTemplate<String, Long> redisTemplate;
    private final ValueOperations<String, Long> valueOperations;
    private final String key = "gomoku-room-id-generator";

    public GomokuRoomIdGenerator(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public Long generateNextRoomId() {
        return valueOperations.increment(key);
    }
}
