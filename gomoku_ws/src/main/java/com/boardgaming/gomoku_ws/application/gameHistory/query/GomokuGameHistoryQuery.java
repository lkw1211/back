package com.boardgaming.gomoku_ws.application.gameHistory.query;

import com.boardgaming.common.exception.room.NotFoundGomokuGameHistoryException;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepository;
import com.boardgaming.domain.gameHistory.dto.response.GomokuGameHistoryResponse;
import com.boardgaming.domain.gameHistory.dto.response.GomokuUserGameHistorySimpleResponse;
import com.boardgaming.domain.room.dto.response.GomokuUserResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.boardgaming.gomoku_ws.application.userHistory.query.GomokuUserHistoryQuery;
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
    private final GomokuUserHistoryQuery gomokuUserHistoryQuery;
    private final UserRepository userRepository;

    public Page<GomokuUserGameHistorySimpleResponse> getUserGameHistory(
        final String userId,
        final Pageable pageable
    ) {
        return gomokuGameHistoryRepository.findAllByUserId(userId, pageable)
            .map(gomokuGameHistory -> GomokuUserGameHistorySimpleResponse.of(
                gomokuGameHistory,
                userId
            ));
    }

    public GomokuGameHistoryResponse getGameHistory(
        final String userId,
        final Long historyId
    ) {
        GomokuGameHistory gomokuGameHistory = gomokuGameHistoryRepository.findById(historyId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        String blackPlayerImageFileUrl = userRepository.findById(gomokuGameHistory.getBlackPlayerId())
                .map(User::getImageFileUrl)
                .orElse(null);

        GomokuUserResponse blackPlayerResponse = GomokuUserResponse.of(
            gomokuGameHistory.getBlackPlayerId(),
            gomokuGameHistory.getBlackPlayerName(),
            gomokuGameHistory.getBlackPlayerEmail(),
            blackPlayerImageFileUrl
        );

        GomokuUserHistoryResponse blackPlayerHistoryResponse = gomokuUserHistoryQuery.getGomokuUserHistory(gomokuGameHistory.getBlackPlayerId());

        String whitePlayerImageFileUrl = userRepository.findById(gomokuGameHistory.getWhitePlayerId())
                .map(User::getImageFileUrl)
                .orElse(null);

        GomokuUserResponse whitePlayerResponse = GomokuUserResponse.of(
            gomokuGameHistory.getWhitePlayerId(),
            gomokuGameHistory.getWhitePlayerName(),
            gomokuGameHistory.getWhitePlayerEmail(),
            whitePlayerImageFileUrl
        );

        GomokuUserHistoryResponse whitePlayerHistoryResponse = gomokuUserHistoryQuery.getGomokuUserHistory(gomokuGameHistory.getWhitePlayerId());

        return GomokuGameHistoryResponse.of(
            blackPlayerResponse,
            blackPlayerHistoryResponse,
            whitePlayerResponse,
            whitePlayerHistoryResponse,
            gomokuGameHistory
        );
    }

    public Optional<GomokuGameHistory> findGameHistory(final Long gameHistoryId) {
        if (Objects.isNull(gameHistoryId)) {
            return Optional.empty();
        }
        return gomokuGameHistoryRepository.findById(gameHistoryId);
    }
}
