package com.boardgaming.domain.gameHistory.domain;

import com.boardgaming.domain.common.BaseTimeEntity;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRule;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuGameHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String blackPlayerId;
    private String blackPlayerName;
    private Long blackPlayerBeforeRating;
    private Long blackPlayerAfterRating;
    private String whitePlayerId;
    private String whitePlayerName;
    private Long whitePlayerBeforeRating;
    private Long whitePlayerAfterRating;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private final List<Integer> moveStack = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private GomokuRule rule;

    private Long turnTime;

    @Enumerated(value = EnumType.STRING)
    private GomokuGameResult result;

    @Enumerated(value = EnumType.STRING)
    private GomokuGameResultReason reason;

    @Builder
    public GomokuGameHistory(
        final String blackPlayerId,
        final String blackPlayerName,
        final String whitePlayerId,
        final String whitePlayerName,
        final Long blackPlayerBeforeRating,
        final Long whitePlayerBeforeRating,
        final GomokuRule rule,
        final Long turnTime,
        final GomokuGameResult result,
        final GomokuGameResultReason reason
    ) {
        this.blackPlayerId = blackPlayerId;
        this.blackPlayerName = blackPlayerName;
        this.blackPlayerBeforeRating = blackPlayerBeforeRating;
        this.whitePlayerId = whitePlayerId;
        this.whitePlayerName = whitePlayerName;
        this.whitePlayerBeforeRating = whitePlayerBeforeRating;
        this.rule = rule;
        this.turnTime = turnTime;
        this.result = result;
        this.reason = reason;
    }

    public void updateAfterRating(
        final Long blackPlayerAfterRating,
        final Long whitePlayerAfterRating
    ) {
        this.blackPlayerAfterRating = blackPlayerAfterRating;
        this.whitePlayerAfterRating = whitePlayerAfterRating;
    }

    public void addMove(final Integer move) {
        if (Objects.nonNull(this.result)) {
            return;
        }

        this.moveStack.add(move);
    }

    public void updateResultAndReason(
        final GomokuGameResult result,
        final GomokuGameResultReason reason
    ) {
        this.result = result;
        this.reason = reason;
    }

    public GomokuColor getCurrentTurn() {
        return this.moveStack.size() % 2 == 0 ? GomokuColor.BLACK : GomokuColor.WHITE;
    }
}
