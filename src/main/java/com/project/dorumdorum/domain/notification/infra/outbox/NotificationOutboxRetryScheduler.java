package com.project.dorumdorum.domain.notification.infra.outbox;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxDeliveryProcessor;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import io.micrometer.core.instrument.MeterRegistry;
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
    private final MeterRegistry meterRegistry;

    @Scheduled(fixedDelay = RETRY_INTERVAL_MS)
    public void run() {
        retryNotificationOutbox();
    }

    private void retryNotificationOutbox() {
        List<NotificationOutbox> outboxes = notificationOutboxService.loadRetryBatch(RETRY_BATCH_SIZE);
        meterRegistry.counter("notification.outbox.retry.batch").increment();
        meterRegistry.counter("notification.outbox.retry.loaded").increment(outboxes.size());

        for (NotificationOutbox outbox : outboxes) {
            try {
                notificationOutboxDeliveryProcessor.processFromOutbox(outbox);
            } catch (Exception e) {
                meterRegistry.counter("notification.outbox.retry.error").increment();
                log.warn("Outbox retry failed outboxNo={}", outbox.getOutboxNo(), e);
            }
        }
    }
}
