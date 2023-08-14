package com.boardgaming.domain.gameHistory.domain.repository;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class GomokuGameHistoryRepositoryTest {
    @Autowired
    private GomokuGameHistoryRepository repository;

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void test1() {
        //given
        User blackPlayer = new User("black", "black@player.com", "blackcalb", "black.example.com", Role.USER, null);
        User whitePlayer = new User("white", "white@player.com", "whitetihw", "white.example.com", Role.USER, null);

        GomokuGameHistory history = GomokuGameHistory.builder()
            .whitePlayerId(whitePlayer.getId())
            .whitePlayerName(whitePlayer.getName())
            .blackPlayerId(blackPlayer.getId())
            .blackPlayerName(blackPlayer.getName())
            .blackPlayerBeforeRating(1500L)
            .whitePlayerBeforeRating(1600L)
            .rule(GomokuRule.RENJU)
            .turnTime(60000L)
            .build();

        //when
        repository.save(history);
        GomokuGameHistory history2 = repository.findById(history.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(history.getBlackPlayerId()).isEqualTo(history2.getBlackPlayerId());
        assertThat(history.getBlackPlayerName()).isEqualTo(history2.getBlackPlayerName());
        assertThat(history.getBlackPlayerBeforeRating()).isEqualTo(history2.getBlackPlayerBeforeRating());
        assertThat(history.getBlackPlayerAfterRating()).isEqualTo(history2.getBlackPlayerAfterRating());
        assertThat(history.getId()).isEqualTo(history2.getId());
        assertThat(history.getWhitePlayerId()).isEqualTo(history2.getWhitePlayerId());
        assertThat(history.getWhitePlayerName()).isEqualTo(history2.getWhitePlayerName());
        assertThat(history.getWhitePlayerBeforeRating()).isEqualTo(history2.getWhitePlayerBeforeRating());
        assertThat(history.getWhitePlayerAfterRating()).isEqualTo(history2.getWhitePlayerAfterRating());
        assertThat(history.getTurnTime()).isEqualTo(history2.getTurnTime());
        assertThat(history.getReason()).isEqualTo(history2.getReason());
        assertThat(history.getResult()).isEqualTo(history2.getResult());
        assertThat(history.getRule()).isEqualTo(history2.getRule());
        assertThat(history.getMoveStack().size()).isEqualTo(history2.getMoveStack().size());
    }
}