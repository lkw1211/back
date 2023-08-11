package com.boardgaming.domain.userHistory.domain.repository;

import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GomokuUserHistoryRepository extends JpaRepository<GomokuUserHistory, Long> {
    Optional<GomokuUserHistory> findByUserId(final String userId);
    List<GomokuUserHistory> findByUserIdIn(final Collection<String> userIdCollection);
    void deleteAllByUserIdIn(final Collection<String> userIds);
}
