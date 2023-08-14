package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.config.QuerydslConfig;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepository;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.room.dto.response.GomokuRoomResponse;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import com.boardgaming.domain.userHistory.domain.repository.GomokuUserHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { GomokuRoomRepositoryCustom.class, QuerydslConfig.class })
@DataJpaTest
class GomokuRoomRepositoryCustomTest {
    @Autowired
    private GomokuRoomRepositoryCustom gomokuRoomRepositoryCustom;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GomokuUserHistoryRepository gomokuUserHistoryRepository;
    @Autowired
    private GomokuGameHistoryRepository gomokuGameHistoryRepository;

    @Test
    @DisplayName("getRoomDetail 메소드 테스트")
    void test1() {
        //given
        User blackPlayer = userRepository.save(User.builder()
            .email("black@player.com")
            .name("blackPlayer")
            .password("black")
            .imageFileUrl("blackUrl")
            .role(Role.USER)
            .build());

        User whitePlayer = userRepository.save(User.builder()
            .email("white@player.com")
            .name("whitePlayer")
            .password("white")
            .imageFileUrl("whiteUrl")
            .role(Role.USER)
            .build());

        GomokuUserHistory blackPlayerHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
                .rating(1200L)
                .userId(blackPlayer.getId())
            .build());

        GomokuUserHistory whitePlayerHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
            .rating(1200L)
            .userId(whitePlayer.getId())
            .build());

        GomokuGameHistory gomokuGameHistory = gomokuGameHistoryRepository.save(GomokuGameHistory.builder()
                .blackPlayerId(blackPlayer.getId())
                .whitePlayerId(whitePlayer.getId())
                .blackPlayerBeforeRating(blackPlayerHistory.getRating())
                .whitePlayerBeforeRating(whitePlayerHistory.getRating())
                .rule(GomokuRule.RENJU)
                .turnTime(60000L)
            .build());

        GomokuRoom room = GomokuRoom.builder()
            .id(1L)
            .rule(GomokuRule.RENJU)
            .turnTime(gomokuGameHistory.getTurnTime())
            .turnTimeEnd(System.currentTimeMillis() + 60 * 1000L)
            .blackPlayer(blackPlayer)
            .whitePlayer(whitePlayer)
            .gomokuGameHistoryId(gomokuGameHistory.getId())
            .gameTurn(GomokuColor.BLACK)
            .build();

        //when
        GomokuRoomResponse response = gomokuRoomRepositoryCustom.getRoomDetail(room);

        //then
        assertThat(response.id()).isEqualTo(room.getId());
        assertThat(response.rule()).isEqualTo(room.getRule());
        assertThat(response.turnTime()).isEqualTo(room.getTurnTime());
        assertThat(response.turnTimeLeft()).isGreaterThan(0L);
        assertThat(response.blackPlayer().id()).isEqualTo(blackPlayer.getId());
        assertThat(response.blackPlayer().name()).isEqualTo(blackPlayer.getName());
        assertThat(response.blackPlayer().email()).isEqualTo(blackPlayer.getEmail());
        assertThat(response.blackPlayer().imageFileUrl()).isEqualTo(blackPlayer.getImageFileUrl());
        assertThat(response.blackGameUserHistory().winCnt()).isEqualTo(blackPlayerHistory.getWinCnt());
        assertThat(response.blackGameUserHistory().loseCnt()).isEqualTo(blackPlayerHistory.getLoseCnt());
        assertThat(response.blackGameUserHistory().drawCnt()).isEqualTo(blackPlayerHistory.getDrawCnt());
        assertThat(response.blackGameUserHistory().rating()).isEqualTo(blackPlayerHistory.getRating());
        assertThat(response.whitePlayer().id()).isEqualTo(whitePlayer.getId());
        assertThat(response.whitePlayer().name()).isEqualTo(whitePlayer.getName());
        assertThat(response.whitePlayer().email()).isEqualTo(whitePlayer.getEmail());
        assertThat(response.whitePlayer().imageFileUrl()).isEqualTo(whitePlayer.getImageFileUrl());
        assertThat(response.whiteGameUserHistory().winCnt()).isEqualTo(whitePlayerHistory.getWinCnt());
        assertThat(response.whiteGameUserHistory().loseCnt()).isEqualTo(whitePlayerHistory.getLoseCnt());
        assertThat(response.whiteGameUserHistory().drawCnt()).isEqualTo(whitePlayerHistory.getDrawCnt());
        assertThat(response.whiteGameUserHistory().rating()).isEqualTo(whitePlayerHistory.getRating());
        for (int i = 0; i < response.moveStack().size(); ++i) {
            assertThat(response.moveStack().get(i)).isEqualTo(gomokuGameHistory.getMoveStack().get(i));
        }
        assertThat(response.gameTurn()).isEqualTo(room.getGameTurn());
        assertThat(response.gameResult()).isEqualTo(gomokuGameHistory.getResult());
        assertThat(response.gameResultReason()).isEqualTo(gomokuGameHistory.getReason());
    }
}