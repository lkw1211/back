package com.boardgaming.gomoku_ws.application.gameQueue.scheduler;

import com.boardgaming.domain.gameQueue.domain.GomokuGameQueue;
import com.boardgaming.gomoku_ws.application.gameQueue.command.GomokuGameQueueService;
import com.boardgaming.gomoku_ws.application.gameQueue.query.GomokuGameQueueQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GomokuGameQueueScheduler {
    private final GomokuGameQueueService gomokuGameQueueService;
    private final GomokuGameQueueQuery gomokuGameQueueQuery;

    @Scheduled(cron = "*/5 * * * * *")
    public void gameQueueScheduler() {
        List<GomokuGameQueue> gomokuGameQueueTimeSortList = new ArrayList<>(gomokuGameQueueQuery.getAllGomokuGameQueueOrderByCreatedDateDesc());
        List<GomokuGameQueue> gomokuGameQueueRatingSortList = new ArrayList<>(gomokuGameQueueTimeSortList.stream().sorted(Comparator.comparing(GomokuGameQueue::getRating, Comparator.reverseOrder())).toList());

        while (gomokuGameQueueTimeSortList.size() > 1) {
            GomokuGameQueue queue = gomokuGameQueueTimeSortList.remove(0);

            int queueIndex = gomokuGameQueueRatingSortList.indexOf(queue);
            List<GomokuGameQueue> opponentCandiQueue = new ArrayList<>();

            if (queueIndex == 0) {
                opponentCandiQueue.add(gomokuGameQueueRatingSortList.get(queueIndex + 1));
            } else if (queueIndex == gomokuGameQueueRatingSortList.size() - 1) {
                opponentCandiQueue.add(gomokuGameQueueRatingSortList.get(queueIndex - 1));
            } else {
                opponentCandiQueue.add(gomokuGameQueueRatingSortList.get(queueIndex + 1));
                opponentCandiQueue.add(gomokuGameQueueRatingSortList.get(queueIndex - 1));
            }

            gomokuGameQueueRatingSortList.remove(queue);

            GomokuGameQueue opponentQueue = opponentCandiQueue.stream()
                .filter(checkQueue -> GomokuGameQueue.isPlayableRating(queue, checkQueue))
                .min(Comparator.comparing(checkQueue -> Math.abs(checkQueue.getRating() - queue.getRating())))
                .orElse(null);

            if (Objects.nonNull(opponentQueue)) {
                gomokuGameQueueTimeSortList.remove(opponentQueue);
                gomokuGameQueueRatingSortList.remove(opponentQueue);
                gomokuGameQueueService.matchGameQueue(List.of(queue.getUserId(), opponentQueue.getUserId()));
            }
        }
    }
}
