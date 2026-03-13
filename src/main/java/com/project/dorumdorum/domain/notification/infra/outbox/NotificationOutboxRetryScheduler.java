package com.project.dorumdorum.domain.notification.infra.outbox;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationOutboxRepository;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationRepository;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryOutboxService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
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
    private final NotificationService notificationService;
    private final NotificationDeliveryOutboxService notificationDeliveryOutboxService;
    private final NotificationOutboxRepository notificationOutboxRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDeviceRepository notificationDeviceRepository;
    private final NotificationDeliveryOrchestrator deliveryOrchestrator;

    // todo: 로직 수정
    @Scheduled(fixedDelay = RETRY_INTERVAL_MS)
    public void run() {
        retryNotificationOutbox();
        processDeliveryOutbox();
    }

    private void retryNotificationOutbox() {
        List<NotificationOutbox> outboxes = notificationOutboxService.loadRetryBatch(RETRY_BATCH_SIZE);

        for (NotificationOutbox outbox : outboxes) {
            try {
                Notification saved = notificationService.save(
                        outbox.getRecipientNo(),
                        outbox.getTitle(),
                        outbox.getBody(),
                        outbox.getType(),
                        outbox.getRelatedId()
                );

                notificationOutboxService.success(outbox.getOutboxNo(), saved.getNotificationNo());
            } catch (Exception e) {
                notificationOutboxService.fail(outbox.getOutboxNo());
                log.warn("Outbox retry failed outboxNo={}", outbox.getOutboxNo(), e);
            }
        }
    }

    private void processDeliveryOutbox() {
        List<NotificationDeliveryOutbox> outboxes = notificationDeliveryOutboxService.loadProcessableBatch(RETRY_BATCH_SIZE);

        for (NotificationDeliveryOutbox outbox : outboxes) {
            try {
                String notificationNo = notificationOutboxRepository.findById(outbox.getNotificationOutboxNo())
                        .orElseThrow(() -> new IllegalStateException("NotificationOutbox not found"))
                        .getNotificationNo();

                Notification notification = notificationRepository.findById(notificationNo)
                        .orElseThrow(() -> new IllegalStateException("Notification not found"));
                Device device = notificationDeviceRepository.findById(outbox.getDeviceNo())
                        .orElseThrow(() -> new IllegalStateException("Device not found"));

                deliveryOrchestrator.deliver(notification, device);
                notificationDeliveryOutboxService.success(outbox.getDeliveryOutboxNo());
            } catch (Exception e) {
                notificationDeliveryOutboxService.fail(outbox.getDeliveryOutboxNo());
                log.warn("Delivery outbox retry failed deliveryOutboxNo={}", outbox.getDeliveryOutboxNo(), e);
            }
        }
    }
}
