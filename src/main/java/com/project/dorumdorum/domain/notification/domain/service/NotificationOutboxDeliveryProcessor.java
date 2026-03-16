package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.application.event.NotificationRequestEvent;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationOutboxDeliveryProcessor {

    private final NotificationService notificationService;
    private final NotificationDeviceService notificationDeviceService;
    private final NotificationDeliveryOrchestrator notificationDeliveryOrchestrator;
    private final NotificationOutboxService notificationOutboxService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processFromEvent(NotificationRequestEvent event) {
        process(
                event.outboxNo(),
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processFromOutbox(NotificationOutbox outbox) {
        process(
                outbox.getOutboxNo(),
                outbox.getRecipientNo(),
                outbox.getTitle(),
                outbox.getBody(),
                outbox.getType(),
                outbox.getRelatedId()
        );
    }

    private void process(
            String outboxNo,
            String recipientNo,
            String title,
            String body,
            NotificationType type,
            String relatedId
    ) {
        try {
            Notification notification = notificationService.save(recipientNo, title, body, type, relatedId);

            NotificationDeliveryOrchestrator.DeliveryResult deliveryResult = notificationDeliveryOrchestrator.deliver(
                    notification,
                    notificationDeviceService.findByUserNo(recipientNo)
            );

            if (!deliveryResult.invalidTokens().isEmpty()) {
                notificationDeviceService.clearInvalidFcmTokens(deliveryResult.invalidTokens());
            }

            if (deliveryResult.hasRetryableFailure()) {
                notificationOutboxService.fail(outboxNo);
                return;
            }

            notificationOutboxService.success(outboxNo, notification.getNotificationNo());
        } catch (Exception e) {
            notificationOutboxService.fail(outboxNo);
            log.warn("Notification delivery failed outboxNo={} recipientNo={}", outboxNo, recipientNo, e);
        }
    }
}
