package com.boardgaming.gomoku_ws.application.gameHistory.query;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepository;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepositoryCustom;
import com.boardgaming.domain.gameHistory.dto.response.GomokuGameHistoryResponse;
import com.boardgaming.domain.gameHistory.dto.response.GomokuUserGameHistorySimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GomokuGameHistoryQuery {
    private final GomokuGameHistoryRepository gomokuGameHistoryRepository;
    private final GomokuGameHistoryRepositoryCustom gomokuGameHistoryCustomRepository;

    public Page<GomokuUserGameHistorySimpleResponse> getUserGameHistory(
        final String userId,
        final Pageable pageable
    ) {
        return gomokuGameHistoryCustomRepository.findAllByUserId(userId, pageable)
            .map(gomokuGameHistory -> {
                boolean isBlackPlayer = gomokuGameHistory.getBlackPlayerId().equals(userId);
                return isBlackPlayer
                    ? GomokuUserGameHistorySimpleResponse.blackPlayerOf(gomokuGameHistory)
                    : GomokuUserGameHistorySimpleResponse.whitePlayerOf(gomokuGameHistory);
            });
    }

    public GomokuGameHistoryResponse getGameHistory(
        final String userId,
        final Long historyId
    ) {
        return gomokuGameHistoryCustomRepository.findByUserIdAndHistoryId(userId, historyId);
    }

    public Optional<GomokuGameHistory> findGameHistory(final Long gameHistoryId) {
        if (Objects.isNull(gameHistoryId)) {
            return Optional.empty();
        }
        return gomokuGameHistoryRepository.findById(gameHistoryId);
    }
}
