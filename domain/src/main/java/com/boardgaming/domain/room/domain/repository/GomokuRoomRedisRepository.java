package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRoomStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GomokuRoomRedisRepository extends CrudRepository<GomokuRoom, Long> {
    List<GomokuRoom> findAllByBlackPlayerIdOrWhitePlayerId(
        final String blackPlayerId,
        final String whitePlayerId
    );

    List<GomokuRoom> findAllByBlackPlayerId(final String playerId);
    List<GomokuRoom> findAllByWhitePlayerId(final String playerId);

    List<GomokuRoom> findAllByStatus(final GomokuRoomStatus status);
    Optional<GomokuRoom> findByIdAndStatus(
        final Long id,
        final GomokuRoomStatus status
    );
}
