package com.boardgaming.domain.gameHistory.domain.repository;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GomokuGameHistoryRepository extends JpaRepository<GomokuGameHistory, Long> {
    @Query(value = """
        SELECT ggh
        FROM GomokuGameHistory ggh
        WHERE ggh.blackPlayerId = :userId or ggh.whitePlayerId = :userId
    """)
    Page<GomokuGameHistory> findAllByUserId(
        @Param("userId") final String userId,
        final Pageable pageable
    );
}
