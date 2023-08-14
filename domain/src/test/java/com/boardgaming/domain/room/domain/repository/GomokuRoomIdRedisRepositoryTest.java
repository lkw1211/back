package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.config.GomokuRoomIdGenerator;
import com.boardgaming.domain.config.RedisConfig;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import redis.embedded.RedisServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { GomokuRoomIdRedisRepository.class, GomokuRoomIdGenerator.class, RedisConfig.class })
@DataRedisTest
class GomokuRoomIdRedisRepositoryTest {
    private static RedisServer redisServer;

    @Autowired
    private GomokuRoomIdRedisRepository repository;
    @Autowired
    private GomokuRoomIdGenerator gomokuRoomIdGenerator;

    @BeforeAll
    static void startRedisServer() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @AfterAll
    static void endRedisServer() {
        redisServer.stop();
    }

    GomokuRoom gomokuRoom1;

    GomokuRoom gomokuRoom2;

    GomokuRoom gomokuRoom3;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        //given
        gomokuRoom1 = GomokuRoom.builder()
            .id(gomokuRoomIdGenerator.generateNextRoomId())
            .turnTimeEnd(System.currentTimeMillis())
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        gomokuRoom2 = GomokuRoom.builder()
            .id(gomokuRoomIdGenerator.generateNextRoomId())
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        gomokuRoom3 = GomokuRoom.builder()
            .id(gomokuRoomIdGenerator.generateNextRoomId())
            .turnTimeEnd(System.currentTimeMillis())
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();
        repository.save(gomokuRoom1);
        repository.save(gomokuRoom2);
        repository.save(gomokuRoom3);
    }

    private static User user1 = User.builder()
        .name("user1")
        .email("user1@email.com")
        .role(Role.USER)
        .imageFileUrl("user1.hello.com")
        .password("user1-password")
        .build();
    private static User user2 = User.builder()
        .name("user2")
        .email("user2@email.com")
        .role(Role.USER)
        .imageFileUrl("user2.hello.com")
        .password("user2-password")
        .build();

    @Test
    @DisplayName("deleteAll 메소드 테스트1")
    void deleteAll1() {
        //when
        repository.deleteAll();

        //then
        assertThat(repository.findAllOrderByDesc().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("deleteAll 메소드 테스트2")
    void deleteAll2() {
        //when
        repository.deleteAll(List.of(gomokuRoom1, gomokuRoom2));
        List<Long> result = repository.findAllOrderByDesc();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.contains(gomokuRoom1.getId())).isFalse();
        assertThat(result.contains(gomokuRoom2.getId())).isFalse();
        assertThat(result.contains(gomokuRoom3.getId())).isTrue();
    }

    @Test
    @DisplayName("deleteById 메소드 테스트")
    void deleteById() {
        //when1
        repository.deleteById(gomokuRoom1.getId());
        //then1
        assertThat(repository.findAllOrderByDesc().size()).isEqualTo(2L);
        //when2
        repository.deleteById(gomokuRoom2.getId());
        //then2
        assertThat(repository.findAllOrderByDesc().size()).isEqualTo(1L);
        //when3
        repository.deleteById(gomokuRoom3.getId());
        //then3
        assertThat(repository.findAllOrderByDesc().size()).isEqualTo(0L);
    }

    @Test
    @DisplayName("findAllOrderByDesc 메소드 테스트")
    void findAllOrderByDesc() {
        //when
        List<Long> ids = repository.findAllOrderByDesc();

        //then
        assertThat(ids.size()).isEqualTo(3);
        assertThat(ids.get(0)).isEqualTo(3L);
        assertThat(ids.get(1)).isEqualTo(2L);
        assertThat(ids.get(2)).isEqualTo(1L);
    }
}