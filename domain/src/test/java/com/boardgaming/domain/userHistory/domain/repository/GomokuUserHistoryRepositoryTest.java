package com.boardgaming.domain.userHistory.domain.repository;

import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class GomokuUserHistoryRepositoryTest {
    @Autowired
    private GomokuUserHistoryRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        repository.saveAll(List.of(history1, history2, history3));
    }

    private static GomokuUserHistory history1 = GomokuUserHistory.builder()
        .userId("userId1")
        .rating(1200L)
        .build();

    private static GomokuUserHistory history2 = GomokuUserHistory.builder()
        .userId("userId2")
        .rating(1300L)
        .build();

    private static GomokuUserHistory history3 = GomokuUserHistory.builder()
        .userId("userId3")
        .rating(1400L)
        .build();

    @Test
    void findByUserId() {
        //when
        GomokuUserHistory result3 = repository.findByUserId(history3.getUserId())
            .orElseThrow(RuntimeException::new);
        GomokuUserHistory result2 = repository.findByUserId(history2.getUserId())
            .orElseThrow(RuntimeException::new);
        GomokuUserHistory result1 = repository.findByUserId(history1.getUserId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(result3.getRating()).isEqualTo(history3.getRating());
        assertThat(result3.getUserId()).isEqualTo(history3.getUserId());
        assertThat(result2.getRating()).isEqualTo(history2.getRating());
        assertThat(result2.getUserId()).isEqualTo(history2.getUserId());
        assertThat(result1.getRating()).isEqualTo(history1.getRating());
        assertThat(result1.getUserId()).isEqualTo(history1.getUserId());
    }

    @Test
    void findByUserIdIn() {
        //when
        Collection<GomokuUserHistory> result = repository.findByUserIdIn(List.of(history1.getUserId(), history2.getUserId()));
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.contains(history1)).isTrue();
        assertThat(result.contains(history2)).isTrue();
        assertThat(result.contains(history3)).isFalse();
    }

    @Test
    void deleteAllByUserIdIn() {
        //when
        repository.deleteAllByUserIdIn(List.of(history1.getUserId(), history2.getUserId()));
        List<GomokuUserHistory> result = repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUserId()).isNotEqualTo(history1.getUserId());
        assertThat(result.get(0).getUserId()).isNotEqualTo(history2.getUserId());
        assertThat(result.get(0).getUserId()).isEqualTo(history3.getUserId());
    }
}