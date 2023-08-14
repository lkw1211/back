package com.boardgaming.domain.userHistory.domain.repository;

import com.boardgaming.domain.config.QuerydslConfig;
import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import(value = { GomokuUserHistoryCustomRepository.class, QuerydslConfig.class })
@DataJpaTest
class GomokuUserHistoryCustomRepositoryTest {
    @Autowired
    private GomokuUserHistoryRepository repository;
    @Autowired
    private GomokuUserHistoryCustomRepository customRepository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("findByUserId 메소드 테스트")
    void findByUserId() {
        //given
        GomokuUserHistory gomokuUserHistory1 = GomokuUserHistory.builder()
            .userId("userId1")
            .rating(1200L)
            .build();

        GomokuUserHistory gomokuUserHistory2 = GomokuUserHistory.builder()
            .userId("userId2")
            .rating(1400L)
            .build();

        repository.saveAll(List.of(gomokuUserHistory1, gomokuUserHistory2));

        //when
        GomokuUserHistoryResponse response1 = customRepository.findByUserId(gomokuUserHistory1.getUserId());
        GomokuUserHistoryResponse response2 = customRepository.findByUserId(gomokuUserHistory2.getUserId());
        GomokuUserHistoryResponse response3 = customRepository.findByUserId("userId3");

        //then
        assertThat(response1.winCnt()).isEqualTo(gomokuUserHistory1.getWinCnt());
        assertThat(response1.loseCnt()).isEqualTo(gomokuUserHistory1.getLoseCnt());
        assertThat(response1.drawCnt()).isEqualTo(gomokuUserHistory1.getDrawCnt());
        assertThat(response1.rating()).isEqualTo(gomokuUserHistory1.getRating());
        assertThat(response2.winCnt()).isEqualTo(gomokuUserHistory2.getWinCnt());
        assertThat(response2.loseCnt()).isEqualTo(gomokuUserHistory2.getLoseCnt());
        assertThat(response2.drawCnt()).isEqualTo(gomokuUserHistory2.getDrawCnt());
        assertThat(response2.rating()).isEqualTo(gomokuUserHistory2.getRating());
        assertThat(response3).isNull();
    }
}