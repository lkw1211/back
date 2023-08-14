package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class GomokuRoomRedisRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private GomokuRoomRedisRepository repository;
    private static final List<User> users = new ArrayList<>();

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
        users.addAll(List.of(
            User.builder()
                .email("user1-email")
                .imageFileUrl("user1-imageFileUrl")
                .role(Role.USER)
                .name("user1-name")
                .password("user1-password")
                .build(),
            User.builder()
                .email("user2-email")
                .imageFileUrl("user2-imageFileUrl")
                .role(Role.USER)
                .name("user2-name")
                .password("user2-password")
                .build(),
            User.builder()
                .email("user3-email")
                .imageFileUrl("user3-imageFileUrl")
                .role(Role.USER)
                .name("user3-name")
                .password("user3-password")
                .build()));
    }

    @Test
    @DisplayName("findByBlackPlayerIdAndWhitePlayerId 메소드 테스트")
    void findByBlackPlayerIdAndWhitePlayerId() {
        //given
        GomokuRoom room1 = GomokuRoom.builder()
            .id(1L)
            .rule(GomokuRule.RENJU)
            .turnTime(60L)
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(users.get(0))
            .whitePlayer(users.get(1))
            .gomokuGameHistoryId(1L)
            .gameTurn(GomokuColor.BLACK)
            .build();

        GomokuRoom room2 = GomokuRoom.builder()
            .id(2L)
            .rule(GomokuRule.RENJU)
            .turnTime(60L)
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(users.get(1))
            .whitePlayer(users.get(2))
            .gomokuGameHistoryId(1L)
            .gameTurn(GomokuColor.BLACK)
            .build();

        GomokuRoom room3 = GomokuRoom.builder()
            .id(3L)
            .rule(GomokuRule.RENJU)
            .turnTime(60L)
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(users.get(2))
            .whitePlayer(users.get(0))
            .gomokuGameHistoryId(1L)
            .gameTurn(GomokuColor.BLACK)
            .build();

        repository.saveAll(List.of(room1, room2, room3));

        //when
        List<GomokuRoom> result1 = repository.findByBlackPlayerIdOrWhitePlayerId(users.get(0).getId(), users.get(0).getId());
        List<GomokuRoom> result2 = repository.findByBlackPlayerIdOrWhitePlayerId(users.get(1).getId(), users.get(1).getId());
        List<GomokuRoom> result3 = repository.findByBlackPlayerIdOrWhitePlayerId(users.get(2).getId(), users.get(2).getId());

        //then
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(2);
        assertThat(result1.stream().map(GomokuRoom::getId).toList().contains(1L)).isTrue();
        assertThat(result1.stream().map(GomokuRoom::getId).toList().contains(3L)).isTrue();
        assertThat(result2.stream().map(GomokuRoom::getId).toList().contains(1L)).isTrue();
        assertThat(result2.stream().map(GomokuRoom::getId).toList().contains(2L)).isTrue();
        assertThat(result3.stream().map(GomokuRoom::getId).toList().contains(2L)).isTrue();
        assertThat(result3.stream().map(GomokuRoom::getId).toList().contains(3L)).isTrue();
    }
}