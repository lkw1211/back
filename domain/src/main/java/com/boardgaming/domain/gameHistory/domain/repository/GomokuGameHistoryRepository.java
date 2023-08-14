package com.boardgaming.domain.gameHistory.domain.repository;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GomokuGameHistoryRepository extends JpaRepository<GomokuGameHistory, Long> {
}
