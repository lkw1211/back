package com.boardgaming.domain.gameQueue.domain.repository;

import com.boardgaming.domain.gameQueue.domain.GomokuGameQueue;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;
import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class GomokuGameQueueRedisRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private GomokuGameQueueRedisRepository repository;

    @BeforeAll
    static void startRedisServer() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @AfterAll
    static void endRedisServer() {
        redisServer.stop();
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void test1() {
        //given
        GomokuGameQueue gomokuGameQueue = GomokuGameQueue.builder()
            .userId("userId")
            .rating(1200L)
            .build();

        //when
        repository.save(gomokuGameQueue);
        GomokuGameQueue gomokuGameQueue2 = repository.findById(gomokuGameQueue.getUserId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(gomokuGameQueue2.getRating()).isEqualTo(gomokuGameQueue.getRating());
        assertThat(gomokuGameQueue2.getUserId()).isEqualTo(gomokuGameQueue.getUserId());
    }
}