package com.boardgaming.domain.userHistory.domain.repository;

import com.boardgaming.domain.userHistory.domain.QGomokuUserHistory;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GomokuUserHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QGomokuUserHistory qGomokuUserHistory = QGomokuUserHistory.gomokuUserHistory;

    public GomokuUserHistoryResponse findByUserId(final String userId) {
        return queryFactory.select(
            Projections.constructor(
                GomokuUserHistoryResponse.class,
                qGomokuUserHistory.winCnt,
                qGomokuUserHistory.loseCnt,
                qGomokuUserHistory.drawCnt,
                qGomokuUserHistory.rating
            ))
            .from(qGomokuUserHistory)
            .where(qGomokuUserHistory.userId.eq(userId))
            .fetchOne();
    }
}
