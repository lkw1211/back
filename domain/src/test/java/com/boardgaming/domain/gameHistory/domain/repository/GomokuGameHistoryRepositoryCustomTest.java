package com.boardgaming.domain.gameHistory.domain.repository;

import com.boardgaming.domain.config.QuerydslConfig;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.dto.response.GomokuGameHistoryResponse;
import com.boardgaming.domain.room.domain.GomokuRule;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { GomokuGameHistoryRepositoryCustom.class, QuerydslConfig.class })
@DataJpaTest
class GomokuGameHistoryRepositoryCustomTest {
    @Autowired
    private GomokuGameHistoryRepositoryCustom repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GomokuUserHistoryRepository gomokuUserHistoryRepository;
    @Autowired
    private GomokuGameHistoryRepository gomokuGameHistoryRepository;

    @Test
    @DisplayName("findByHistoryId 메소드 테스트")
    void test1() {
        //given
        User blackPlayer = userRepository.save(User.builder()
            .imageFileUrl("blackUrl")
            .role(Role.USER)
            .email("black@player.mail")
            .name("black")
            .password("blackPassword")
            .build());

        User whitePlayer = userRepository.save(User.builder()
            .imageFileUrl("whiteUrl")
            .role(Role.USER)
            .email("white@player.mail")
            .name("white")
            .password("whitePassword")
            .build());

        GomokuUserHistory blackHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
            .rating(1200L)
            .userId(blackPlayer.getId())
            .build());

        GomokuUserHistory whiteHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
            .rating(1200L)
            .userId(whitePlayer.getId())
            .build());

        GomokuGameHistory gameHistory = gomokuGameHistoryRepository.save(
            GomokuGameHistory.builder()
                .whitePlayerBeforeRating(1200L)
                .whitePlayerId(whitePlayer.getId())
                .whitePlayerName(whitePlayer.getName())
                .turnTime(60000L)
                .blackPlayerBeforeRating(1200L)
                .blackPlayerId(blackPlayer.getId())
                .blackPlayerName(blackPlayer.getName())
                .rule(GomokuRule.RENJU)
                .build());

        //when
        GomokuGameHistoryResponse response1 = repository.findByUserIdAndHistoryId(blackPlayer.getId(), gameHistory.getId());

        //then
        assertThat(response1.id()).isEqualTo(gameHistory.getId());
        assertThat(response1.blackPlayer().id()).isEqualTo(blackPlayer.getId());
        assertThat(response1.blackPlayer().name()).isEqualTo(blackPlayer.getName());
        assertThat(response1.blackPlayer().email()).isEqualTo(blackPlayer.getEmail());
        assertThat(response1.blackPlayer().imageFileUrl()).isEqualTo(blackPlayer.getImageFileUrl());
        assertThat(response1.blackGameUserHistory().winCnt()).isEqualTo(blackHistory.getWinCnt());
        assertThat(response1.blackGameUserHistory().loseCnt()).isEqualTo(blackHistory.getLoseCnt());
        assertThat(response1.blackGameUserHistory().drawCnt()).isEqualTo(blackHistory.getDrawCnt());
        assertThat(response1.blackGameUserHistory().rating()).isEqualTo(blackHistory.getRating());
        assertThat(response1.blackPlayerBeforeRating()).isEqualTo(gameHistory.getBlackPlayerBeforeRating());
        assertThat(response1.blackPlayerAfterRating()).isEqualTo(gameHistory.getBlackPlayerAfterRating());
        assertThat(response1.whitePlayer().id()).isEqualTo(whitePlayer.getId());
        assertThat(response1.whitePlayer().name()).isEqualTo(whitePlayer.getName());
        assertThat(response1.whitePlayer().email()).isEqualTo(whitePlayer.getEmail());
        assertThat(response1.whitePlayer().imageFileUrl()).isEqualTo(whitePlayer.getImageFileUrl());
        assertThat(response1.whiteGameUserHistory().winCnt()).isEqualTo(whiteHistory.getWinCnt());
        assertThat(response1.whiteGameUserHistory().loseCnt()).isEqualTo(whiteHistory.getLoseCnt());
        assertThat(response1.whiteGameUserHistory().drawCnt()).isEqualTo(whiteHistory.getDrawCnt());
        assertThat(response1.whiteGameUserHistory().rating()).isEqualTo(whiteHistory.getRating());
        assertThat(response1.whitePlayerBeforeRating()).isEqualTo(gameHistory.getWhitePlayerBeforeRating());
        assertThat(response1.whitePlayerAfterRating()).isEqualTo(gameHistory.getWhitePlayerAfterRating());
        assertThat(response1.gameResult()).isNull();
        assertThat(response1.gameResultReason()).isNull();
    }

    @Test
    @DisplayName("findAllByUserId 메소드 테스트")
    void test2() {
        // given
        User blackPlayer = userRepository.save(User.builder()
            .imageFileUrl("blackUrl")
            .role(Role.USER)
            .email("black@player.mail")
            .name("black")
            .password("blackPassword")
            .build());

        User whitePlayer = userRepository.save(User.builder()
            .imageFileUrl("whiteUrl")
            .role(Role.USER)
            .email("white@player.mail")
            .name("white")
            .password("whitePassword")
            .build());

        GomokuUserHistory blackHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
            .rating(1200L)
            .userId(blackPlayer.getId())
            .build());

        GomokuUserHistory whiteHistory = gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
            .rating(1200L)
            .userId(whitePlayer.getId())
            .build());

        GomokuGameHistory gameHistory = gomokuGameHistoryRepository.save(
            GomokuGameHistory.builder()
                .whitePlayerBeforeRating(1200L)
                .whitePlayerId(whitePlayer.getId())
                .whitePlayerName(whitePlayer.getName())
                .turnTime(60000L)
                .blackPlayerBeforeRating(1200L)
                .blackPlayerId(blackPlayer.getId())
                .blackPlayerName(blackPlayer.getName())
                .rule(GomokuRule.RENJU)
                .build());

        // when
        Page<GomokuGameHistory> result =
            repository.findAllByUserId(blackPlayer.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        GomokuGameHistory gomokuGameHistory = result.getContent().get(0);

        assertThat(gomokuGameHistory.getBlackPlayerId()).isEqualTo(gameHistory.getBlackPlayerId());
        assertThat(gomokuGameHistory.getBlackPlayerName()).isEqualTo(gameHistory.getBlackPlayerName());
        assertThat(gomokuGameHistory.getBlackPlayerBeforeRating()).isEqualTo(gameHistory.getBlackPlayerBeforeRating());
        assertThat(gomokuGameHistory.getBlackPlayerAfterRating()).isEqualTo(gameHistory.getBlackPlayerAfterRating());
        assertThat(gomokuGameHistory.getId()).isEqualTo(gameHistory.getId());
        assertThat(gomokuGameHistory.getWhitePlayerId()).isEqualTo(gameHistory.getWhitePlayerId());
        assertThat(gomokuGameHistory.getWhitePlayerName()).isEqualTo(gameHistory.getWhitePlayerName());
        assertThat(gomokuGameHistory.getWhitePlayerBeforeRating()).isEqualTo(gameHistory.getWhitePlayerBeforeRating());
        assertThat(gomokuGameHistory.getWhitePlayerAfterRating()).isEqualTo(gameHistory.getWhitePlayerAfterRating());
        assertThat(gomokuGameHistory.getTurnTime()).isEqualTo(gameHistory.getTurnTime());
        assertThat(gomokuGameHistory.getReason()).isEqualTo(gameHistory.getReason());
        assertThat(gomokuGameHistory.getResult()).isEqualTo(gameHistory.getResult());
        assertThat(gomokuGameHistory.getRule()).isEqualTo(gameHistory.getRule());
        assertThat(gomokuGameHistory.getMoveStack().size()).isEqualTo(gameHistory.getMoveStack().size());
    }
}