package com.boardgaming.gomoku_ws.application.gameQueue.query;

import com.boardgaming.domain.gameQueue.domain.GomokuGameQueue;
import com.boardgaming.domain.gameQueue.domain.repository.GomokuGameQueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GomokuGameQueueQuery {
    private final GomokuGameQueueRedisRepository gomokuGameQueueRedisRepository;

    public List<GomokuGameQueue> getAllGomokuGameQueueOrderByCreatedDateDesc() {
        return StreamSupport.stream(gomokuGameQueueRedisRepository.findAll().spliterator(), false)
            .sorted(Comparator.comparing(GomokuGameQueue::getCreatedDate, Comparator.reverseOrder()))
            .toList();
    }
}
