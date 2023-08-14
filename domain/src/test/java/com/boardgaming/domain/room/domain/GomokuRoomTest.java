package com.boardgaming.domain.room.domain;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GomokuRoomTest {
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
    private static User user3 = User.builder()
        .name("user3")
        .email("user3@email.com")
        .role(Role.USER)
        .imageFileUrl("user3.hello.com")
        .password("user3-password")
        .build();

    @Test
    @DisplayName("includePlayer 메소드 테스트")
    void includePlayer() {
        //given
        GomokuRoom gomokuRoom = GomokuRoom.builder()
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        //when
        boolean includePlayer1 = gomokuRoom.includePlayer(user1.getId());
        boolean includePlayer2 = gomokuRoom.includePlayer(user2.getId());
        boolean includePlayer3 = gomokuRoom.includePlayer(user3.getId());

        //then
        assertThat(includePlayer1).isEqualTo(true);
        assertThat(includePlayer2).isEqualTo(true);
        assertThat(includePlayer3).isEqualTo(false);
    }

    @Test
    @DisplayName("isTurnOf 메소드 테스트")
    void isTurnOf() {
        //given
        GomokuRoom gomokuRoom1 = GomokuRoom.builder()
            .blackPlayer(user2)
            .whitePlayer(user3)
            .gameTurn(GomokuColor.BLACK)
            .build();

        GomokuRoom gomokuRoom2 = GomokuRoom.builder()
            .blackPlayer(user3)
            .whitePlayer(user1)
            .gameTurn(GomokuColor.WHITE)
            .build();

        //when
        boolean room1IsTurnOfPlayer1 = gomokuRoom1.isTurnOf(user1.getId());
        boolean room1IsTurnOfPlayer2 = gomokuRoom1.isTurnOf(user2.getId());
        boolean room1IsTurnOfPlayer3 = gomokuRoom1.isTurnOf(user3.getId());
        boolean room2IsTurnOfPlayer1 = gomokuRoom2.isTurnOf(user1.getId());
        boolean room2IsTurnOfPlayer2 = gomokuRoom2.isTurnOf(user2.getId());
        boolean room2IsTurnOfPlayer3 = gomokuRoom2.isTurnOf(user3.getId());

        //then
        assertThat(room1IsTurnOfPlayer1).isEqualTo(false);
        assertThat(room1IsTurnOfPlayer2).isEqualTo(true);
        assertThat(room1IsTurnOfPlayer3).isEqualTo(false);
        assertThat(room2IsTurnOfPlayer1).isEqualTo(true);
        assertThat(room2IsTurnOfPlayer2).isEqualTo(false);
        assertThat(room2IsTurnOfPlayer3).isEqualTo(false);
    }

    @Test
    @DisplayName("updateTurnState 메소드 테스트")
    void updateTurnState() {
        //given
        GomokuRoom gomokuRoom1 = GomokuRoom.builder()
            .blackPlayer(user1)
            .whitePlayer(user2)
            .gameTurn(GomokuColor.BLACK)
            .turnTime(60000L)
            .build();

        //when
        long before = System.currentTimeMillis();
        gomokuRoom1.updateTurnState();
        long after = System.currentTimeMillis();

        //then
        assertThat(gomokuRoom1.getGameTurn()).isEqualTo(GomokuColor.WHITE);
        assertThat(gomokuRoom1.getPieceCnt()).isEqualTo(1L);
        assertThat(gomokuRoom1.getTurnTimeEnd()).isGreaterThanOrEqualTo(before + 60000L * 1000);
        assertThat(gomokuRoom1.getTurnTimeEnd()).isLessThanOrEqualTo(System.currentTimeMillis() + 60000L * 1000);
    }

    @Test
    @DisplayName("needEndUpdate 메소드 테스트")
    void needEndUpdate() {
        //given
        GomokuRoom gomokuRoom1 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis())
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom2 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom3 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis())
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom4 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom5 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis())
            .gomokuGameHistoryId(123L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom6 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .gomokuGameHistoryId(123L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom7 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis())
            .gomokuGameHistoryId(123L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        GomokuRoom gomokuRoom8 = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .gomokuGameHistoryId(123L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();
        
        //when
        boolean needUpdateRoom1 = gomokuRoom1.isEnd();
        boolean needUpdateRoom2 = gomokuRoom2.isEnd();
        boolean needUpdateRoom3 = gomokuRoom3.isEnd();
        boolean needUpdateRoom4 = gomokuRoom4.isEnd();
        boolean needUpdateRoom5 = gomokuRoom5.isEnd();
        boolean needUpdateRoom6 = gomokuRoom6.isEnd();
        boolean needUpdateRoom7 = gomokuRoom7.isEnd();
        boolean needUpdateRoom8 = gomokuRoom8.isEnd();

        //then
        assertThat(needUpdateRoom1).isEqualTo(true);
        assertThat(needUpdateRoom2).isEqualTo(false);
        assertThat(needUpdateRoom3).isEqualTo(true);
        assertThat(needUpdateRoom4).isEqualTo(false);
        assertThat(needUpdateRoom5).isEqualTo(true);
        assertThat(needUpdateRoom6).isEqualTo(false);
        assertThat(needUpdateRoom7).isEqualTo(true);
        assertThat(needUpdateRoom8).isEqualTo(false);
    }

    @Test
    @DisplayName("notModifiedMoreThanThreeMinutes 메소드 테스트")
    void notModifiedMoreThanThreeMinutes() {
        //given
        GomokuRoom gomokuRoom = GomokuRoom.builder()
            .turnTimeEnd(System.currentTimeMillis())
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        //when
        boolean result = gomokuRoom.needDelete();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("doMove, getFiveInRowColor 메소드 테스트")
    void doMove() {
        //given
        GomokuRoom gomokuRoom = GomokuRoom.builder()
            .turnTime(30L)
            .turnTimeEnd(System.currentTimeMillis() + 30L * 1000)
            .gameTurn(GomokuColor.BLACK)
            .rule(GomokuRule.RENJU)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        List<Integer> moveStack = List.of(
            GomokuBoard.makeMove(5,7), GomokuBoard.makeMove(5, 6),
            GomokuBoard.makeMove(6,7), GomokuBoard.makeMove(6, 6),
            GomokuBoard.makeMove(7,7), GomokuBoard.makeMove(7, 6),
            GomokuBoard.makeMove(8,7), GomokuBoard.makeMove(8, 6),
            GomokuBoard.makeMove(9,7), GomokuBoard.makeMove(9, 6)
        );

        GomokuColor[] expected = new GomokuColor[] {
            null, null,
            null, null,
            null, null,
            null, null,
            GomokuColor.BLACK, GomokuColor.BLACK
        };

        //when //then
        for (int i = 0; i < moveStack.size(); ++i) {
            gomokuRoom.doMove(moveStack.get(i));
            assertThat(gomokuRoom.getFiveInRowColor()).isEqualTo(expected[i]);
        }
    }
}