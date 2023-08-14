package com.boardgaming.domain.gameHistory.dto.response;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResult;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResultReason;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.userHistory.domain.GomokuUserResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GomokuUserGameHistorySimpleResponseTest {
    @Test
    @DisplayName("blackPlayerOf, whitePlayerOf 메소드 테스트")
    void test1() {
        //given
        User blackPlayer = new User("black", "black@player.com", "blackcalb", "black.example.com", Role.USER, null);
        User whitePlayer = new User("white", "white@player.com", "whitetihw", "white.example.com", Role.USER, null);
        GomokuGameHistory history = GomokuGameHistory.builder()
            .blackPlayerId(blackPlayer.getId())
            .blackPlayerName(blackPlayer.getName())
            .whitePlayerId(whitePlayer.getId())
            .whitePlayerName(whitePlayer.getName())
            .blackPlayerBeforeRating(1600L)
            .whitePlayerBeforeRating(1500L)
            .rule(GomokuRule.RENJU)
            .turnTime(60000L)
            .build();

        history.updateAfterRating(1550L, 1550L);
        history.updateResultAndReason(GomokuGameResult.WHITE_WIN, GomokuGameResultReason.DEFAULT);

        //when
        GomokuUserGameHistorySimpleResponse blackResponse = GomokuUserGameHistorySimpleResponse.blackPlayerOf(history);
        GomokuUserGameHistorySimpleResponse whiteResponse = GomokuUserGameHistorySimpleResponse.whitePlayerOf(history);

        //then
        assertThat(blackResponse.id()).isEqualTo(history.getId());
        assertThat(blackResponse.beforeRating()).isEqualTo(1600L);
        assertThat(blackResponse.afterRating()).isEqualTo(1550L);
        assertThat(blackResponse.userColor()).isEqualTo(GomokuColor.BLACK);
        assertThat(blackResponse.opponent()).isEqualTo(whitePlayer.getName());
        assertThat(blackResponse.result()).isEqualTo(GomokuUserResult.LOSE);
        assertThat(whiteResponse.id()).isEqualTo(history.getId());
        assertThat(whiteResponse.beforeRating()).isEqualTo(1500L);
        assertThat(whiteResponse.afterRating()).isEqualTo(1550L);
        assertThat(whiteResponse.userColor()).isEqualTo(GomokuColor.WHITE);
        assertThat(whiteResponse.opponent()).isEqualTo(blackPlayer.getName());
        assertThat(whiteResponse.result()).isEqualTo(GomokuUserResult.WIN);
    }
}