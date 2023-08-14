package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.gameHistory.domain.QGomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.dto.response.GomokuRoomResponse;
import com.boardgaming.domain.room.dto.response.GomokuUserResponse;
import com.boardgaming.domain.user.domain.QUser;
import com.boardgaming.domain.userHistory.domain.QGomokuUserHistory;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GomokuRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final QUser blackPlayer = new QUser("blackPlayer");
    private static final QUser whitePlayer = new QUser("whitePlayer");
    private static final QGomokuUserHistory blackPlayerHistory = new QGomokuUserHistory("blackPlayerHistory");
    private static final QGomokuUserHistory whitePlayerHistory = new QGomokuUserHistory("whitePlayerHistory");
    private static final QGomokuGameHistory gomokuGameHistory = QGomokuGameHistory.gomokuGameHistory;

    public GomokuRoomResponse getRoomDetail(final GomokuRoom room) {
        Expression<Long> turnTimeLeftExpression = Expressions.constant(Optional.ofNullable(room.getTurnTimeEnd())
            .map(timeEnd -> timeEnd - System.currentTimeMillis())
            .orElse(0L));

        Expression<GomokuUserResponse> blackPlayerResponseExpression = Projections.constructor(
            GomokuUserResponse.class,
            Expressions.constant(room.getBlackPlayerId()),
            blackPlayer.name,
            blackPlayer.email,
            blackPlayer.imageFileUrl
        );

        Expression<GomokuUserHistoryResponse> blackPlayerHistoryResponseExpression = Projections.constructor(
            GomokuUserHistoryResponse.class,
            blackPlayerHistory.winCnt,
            blackPlayerHistory.loseCnt,
            blackPlayerHistory.drawCnt,
            blackPlayerHistory.rating
        );

        Expression<GomokuUserResponse> whitePlayerResponseExpression = Projections.constructor(
            GomokuUserResponse.class,
            Expressions.constant(room.getWhitePlayerId()),
            whitePlayer.name,
            whitePlayer.email,
            whitePlayer.imageFileUrl
        );

        Expression<GomokuUserHistoryResponse> whitePlayerHistoryResponseExpression = Projections.constructor(
            GomokuUserHistoryResponse.class,
            whitePlayerHistory.winCnt,
            whitePlayerHistory.loseCnt,
            whitePlayerHistory.drawCnt,
            whitePlayerHistory.rating
        );

        return queryFactory.select(
            Projections.constructor(
                GomokuRoomResponse.class,
                Expressions.constant(room.getId()),
                Expressions.constant(room.getRule()),
                Expressions.constant(room.getTurnTime()),
                turnTimeLeftExpression,
                blackPlayerResponseExpression,
                blackPlayerHistoryResponseExpression,
                whitePlayerResponseExpression,
                whitePlayerHistoryResponseExpression,
                gomokuGameHistory.moveStack,
                Expressions.constant(room.getGameTurn()),
                gomokuGameHistory.result,
                gomokuGameHistory.reason
            ))
            .from(gomokuGameHistory)
            .where(gomokuGameHistory.id.eq(Expressions.constant(room.getGomokuGameHistoryId())))
            .innerJoin(blackPlayer).on(blackPlayer.id.eq(gomokuGameHistory.blackPlayerId))
            .innerJoin(whitePlayer).on(whitePlayer.id.eq(gomokuGameHistory.whitePlayerId))
            .innerJoin(blackPlayerHistory).on(blackPlayerHistory.userId.eq(gomokuGameHistory.blackPlayerId))
            .innerJoin(whitePlayerHistory).on(whitePlayerHistory.userId.eq(gomokuGameHistory.whitePlayerId))
            .fetchOne();
    }
}
