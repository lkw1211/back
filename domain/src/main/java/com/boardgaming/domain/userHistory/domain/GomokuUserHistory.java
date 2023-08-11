package com.boardgaming.domain.userHistory.domain;

import com.boardgaming.domain.common.BaseTimeEntity;
import com.boardgaming.domain.room.domain.GomokuColor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuUserHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Long winCnt;
    private Long loseCnt;
    private Long drawCnt;
    private Long blackPlayCnt;
    private Long whitePlayCnt;
    private Long rating;

    @Builder
    public GomokuUserHistory(
        final String userId,
        final Long rating
    ) {
        this.userId = userId;
        this.winCnt = 0L;
        this.loseCnt = 0L;
        this.drawCnt = 0L;
        this.blackPlayCnt = 0L;
        this.whitePlayCnt = 0L;
        this.rating = rating;
    }

    public void updateHistory(
        final GomokuUserResult result,
        final GomokuColor playColor,
        final Long otherRating
    ) {
        this.rating += getRatingChange(
            this.rating,
            otherRating,
            getRatingWeight(this.blackPlayCnt + this.whitePlayCnt, this.rating),
            result
        );

        switch (result) {
            case WIN -> this.winCnt += 1;
            case LOSE -> this.loseCnt += 1;
            case DRAW -> this.drawCnt += 1;
        }
        switch (playColor) {
            case BLACK -> this.blackPlayCnt += 1;
            case WHITE -> this.whitePlayCnt += 1;
            case NONE -> throw new UnsupportedOperationException();
        }
    }

    private static Long getRatingChange(
        final Long targetRating,
        final Long otherRating,
        final Double weight,
        final GomokuUserResult result
    ) {
        return (long) (weight * (
            switch (result) {
                case WIN -> 1;
                case DRAW -> 0.5;
                case LOSE -> 0;
            }
            - getExpectedScore(targetRating, otherRating)
        ));
    }

    private static Double getRatingWeight(
        final Long totalPlayCnt,
        final Long targetRating
    ) {
        double weight = 10.0;

        if (targetRating < 1500) {
            weight += 10;
        } else if (targetRating < 1800) {
            weight += 7.5;
        } else if (targetRating < 2100) {
            weight += 5;
        } else if (targetRating < 2400) {
            weight += 2.5;
        }

        long playCntModifier = totalPlayCnt <= 30 ? 2L : 1L;

        return playCntModifier * weight;
    }

    private static Double getExpectedScore(
        final Long targetRating,
        final Long otherRating
    ) {
        return 1 / (Math.pow(10, (otherRating.doubleValue() - targetRating.doubleValue()) / 400) + 1);
    }

    public double getBlackPlayRate() {
        long totalPlayCnt = blackPlayCnt + whitePlayCnt;
        return totalPlayCnt == 0 ? 0.5D : blackPlayCnt.doubleValue() / (double) totalPlayCnt;
    }
}
