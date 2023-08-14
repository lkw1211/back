package com.boardgaming.domain.gameHistory.domain;

import com.boardgaming.domain.room.domain.GomokuBoard;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.room.domain.GomokuRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GomokuGameHistoryTest {
    // given1
    User blackPlayer = new User("black", "black@player.com", "blackcalb", "black.example.com", Role.USER, null);
    User whitePlayer = new User("white", "white@player.com", "whitetihw", "white.example.com", Role.USER, null);

    @Test
    @DisplayName("updateAfterRating 메소드 테스트")
    void updateAfterRating() {
        //given2
        Long beforeRatingBlack = 1500L;
        Long beforeRatingWhite = 1600L;
        Long afterRatingBlack = 1550L;
        Long afterRatingWhite = 1550L;

        GomokuGameHistory history = GomokuGameHistory.builder()
            .whitePlayerId(whitePlayer.getId())
            .whitePlayerName(whitePlayer.getName())
            .blackPlayerId(blackPlayer.getId())
            .blackPlayerName(blackPlayer.getName())
            .blackPlayerBeforeRating(beforeRatingBlack)
            .whitePlayerBeforeRating(beforeRatingWhite)
            .rule(GomokuRule.RENJU)
            .turnTime(60000L)
            .build();

        //when
        history.updateAfterRating(afterRatingBlack, afterRatingWhite);

        //then
        assertThat(history.getBlackPlayerBeforeRating()).isEqualTo(beforeRatingBlack);
        assertThat(history.getWhitePlayerBeforeRating()).isEqualTo(beforeRatingWhite);
        assertThat(history.getBlackPlayerAfterRating()).isEqualTo(afterRatingBlack);
        assertThat(history.getWhitePlayerAfterRating()).isEqualTo(afterRatingWhite);
    }

    @Test
    @DisplayName("addMove 메소드 테스트1")
    void addMove1() {
        //given2
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

        List<Integer> moveStack = List.of(
            GomokuBoard.makeMove(5, 7), GomokuBoard.makeMove(5,6),
            GomokuBoard.makeMove(6, 7), GomokuBoard.makeMove(6,6),
            GomokuBoard.makeMove(7, 7), GomokuBoard.makeMove(7,6),
            GomokuBoard.makeMove(8, 7), GomokuBoard.makeMove(8,6),
            GomokuBoard.makeMove(9, 7), GomokuBoard.makeMove(9,6)
        );

        //when
        moveStack.forEach(history::addMove);

        //then
        for (int i = 0; i < history.getMoveStack().size(); ++i) {
            assertThat(history.getMoveStack().get(i)).isEqualTo(moveStack.get(i));
        }
    }

    @Test
    @DisplayName("addMove 메소드, updateResultAndReason 테스트")
    void addMoveAndUpdateResultAndReason() {
        //given2
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

        List<Integer> moveStack = List.of(
            GomokuBoard.makeMove(5, 7), GomokuBoard.makeMove(5,6),
            GomokuBoard.makeMove(6, 7), GomokuBoard.makeMove(6,6),
            GomokuBoard.makeMove(7, 7), GomokuBoard.makeMove(7,6),
            GomokuBoard.makeMove(8, 7), GomokuBoard.makeMove(8,6),
            GomokuBoard.makeMove(9, 7), GomokuBoard.makeMove(9,6)
        );

        //when
        for (int i = 0; i < moveStack.size(); ++i) {
            history.addMove(moveStack.get(i));
            if (i == moveStack.size() - 2) {
                history.updateResultAndReason(GomokuGameResult.BLACK_WIN, GomokuGameResultReason.DEFAULT);
            }
        }

        //then
        assertThat(history.getMoveStack().size()).isEqualTo(moveStack.size() - 1);
        for (int i = 0; i < history.getMoveStack().size(); ++i) {
            assertThat(history.getMoveStack().get(i)).isEqualTo(moveStack.get(i));
        }
    }

    @Test
    @DisplayName("getCurrentTurn 메소드 테스트")
    void getCurrentTurn() {
        //given2
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

        List<Integer> moveStack = List.of(
            GomokuBoard.makeMove(5, 7), GomokuBoard.makeMove(5,6),
            GomokuBoard.makeMove(6, 7), GomokuBoard.makeMove(6,6),
            GomokuBoard.makeMove(7, 7), GomokuBoard.makeMove(7,6),
            GomokuBoard.makeMove(8, 7), GomokuBoard.makeMove(8,6),
            GomokuBoard.makeMove(9, 7), GomokuBoard.makeMove(9,6)
        );

        //when //then
        assertThat(history.getCurrentTurn()).isEqualTo(GomokuColor.BLACK);

        for (int i = 0; i < moveStack.size(); ++i) {
            history.addMove(moveStack.get(i));
            assertThat(history.getCurrentTurn()).isEqualTo(i % 2 == 0 ? GomokuColor.WHITE : GomokuColor.BLACK);
        }
    }
}