package com.project.dorumdorum.domain.notification.infra.outbox;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxDeliveryProcessor;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationOutboxRetryScheduler {

    private static final int RETRY_INTERVAL_MS = 5_000;
    private static final int RETRY_BATCH_SIZE = 100;

    private final NotificationOutboxService notificationOutboxService;
    private final NotificationOutboxDeliveryProcessor notificationOutboxDeliveryProcessor;

    @Scheduled(fixedDelay = RETRY_INTERVAL_MS)
    public void run() {
        retryNotificationOutbox();
    }

    private void retryNotificationOutbox() {
        List<NotificationOutbox> outboxes = notificationOutboxService.loadRetryBatch(RETRY_BATCH_SIZE);

        for (NotificationOutbox outbox : outboxes) {
            try {
                notificationOutboxDeliveryProcessor.processFromOutbox(outbox);
            } catch (Exception e) {
                log.warn("Outbox retry failed outboxNo={}", outbox.getOutboxNo(), e);
            }
        }
    }

}
