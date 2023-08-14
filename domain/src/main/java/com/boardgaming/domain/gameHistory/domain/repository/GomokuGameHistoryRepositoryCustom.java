package com.boardgaming.domain.gameHistory.domain.repository;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.QGomokuGameHistory;
import com.boardgaming.domain.gameHistory.dto.response.GomokuGameHistoryResponse;
import com.boardgaming.domain.room.dto.response.GomokuUserResponse;
import com.boardgaming.domain.user.domain.QUser;
import com.boardgaming.domain.userHistory.domain.QGomokuUserHistory;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GomokuGameHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final QGomokuGameHistory qGameHistory = QGomokuGameHistory.gomokuGameHistory;

    public GomokuGameHistoryResponse findByUserIdAndHistoryId(
        final String userId,
        final Long historyId
    ) {
        QUser qBlackPlayer = new QUser("qBlackPlayer");
        QGomokuUserHistory qBlackHistory = new QGomokuUserHistory("qBlackHistory");
        QUser qWhitePlayer = new QUser("qWhitePlayer");
        QGomokuUserHistory qWhiteHistory = new QGomokuUserHistory("qWhiteHistory");

        return queryFactory.select(
            Projections.constructor(
                GomokuGameHistoryResponse.class,
                qGameHistory.id,
                Projections.constructor(
                    GomokuUserResponse.class,
                    qBlackPlayer.id,
                    qBlackPlayer.name,
                    qBlackPlayer.email,
                    qBlackPlayer.imageFileUrl
                ),
                Projections.constructor(
                    GomokuUserHistoryResponse.class,
                    qBlackHistory.winCnt,
                    qBlackHistory.loseCnt,
                    qBlackHistory.drawCnt,
                    qBlackHistory.rating
                ),
                qGameHistory.blackPlayerBeforeRating,
                qGameHistory.blackPlayerAfterRating,
                Projections.constructor(
                    GomokuUserResponse.class,
                    qWhitePlayer.id,
                    qWhitePlayer.name,
                    qWhitePlayer.email,
                    qWhitePlayer.imageFileUrl
                ),
                Projections.constructor(
                    GomokuUserHistoryResponse.class,
                    qWhiteHistory.winCnt,
                    qWhiteHistory.loseCnt,
                    qWhiteHistory.drawCnt,
                    qWhiteHistory.rating
                ),
                qGameHistory.whitePlayerBeforeRating,
                qGameHistory.whitePlayerAfterRating,
                qGameHistory.result,
                qGameHistory.reason,
                qGameHistory.moveStack
            ))
            .from(qGameHistory)
            .innerJoin(qBlackPlayer).on(qGameHistory.blackPlayerId.eq(qBlackPlayer.id))
            .innerJoin(qWhitePlayer).on(qGameHistory.whitePlayerId.eq(qWhitePlayer.id))
            .innerJoin(qBlackHistory).on(qGameHistory.blackPlayerId.eq(qBlackHistory.userId))
            .innerJoin(qWhiteHistory).on(qGameHistory.whitePlayerId.eq(qWhiteHistory.userId))
            .where(qGameHistory.id.eq(historyId)
                .or(playerIdIs(userId)))
            .fetchOne();
    }

    public Page<GomokuGameHistory> findAllByUserId(
        final String userId,
        final Pageable pageable
    ) {

        List<GomokuGameHistory> responseList = queryFactory.selectFrom(qGameHistory)
            .where(playerIdIs(userId))
            .orderBy(qGameHistory.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Objects.requireNonNull(queryFactory.select(qGameHistory.count())
            .from(qGameHistory)
            .where(playerIdIs(userId))
            .fetchOne());

        return new PageImpl<>(responseList, pageable, total);
    }

    private static BooleanExpression playerIdIs(String userId) {
        return qGameHistory.blackPlayerId.eq(userId)
            .or(qGameHistory.whitePlayerId.eq(userId));
    }
}
