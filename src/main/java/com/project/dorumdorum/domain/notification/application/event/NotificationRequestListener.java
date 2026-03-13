package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationService notificationService;
    private final NotificationOutboxService notificationOutboxService;

    @Async("notificationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationRequestEvent event) {
        try {
            Notification saved = notificationService.save(
                    event.recipientNo(),
                    event.title(),
                    event.body(),
                    event.type(),
                    event.relatedId()
            );

            notificationOutboxService.success(event.outboxNo(), saved.getNotificationNo());
        } catch (Exception e) {
            notificationOutboxService.fail(event.outboxNo());
            log.error("Failed to process notification request event", e);
            throw e;
        }
    }
}
