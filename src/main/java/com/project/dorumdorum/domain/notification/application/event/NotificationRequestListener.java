package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationService notificationService;
    private final NotificationDeliveryOrchestrator deliveryOrchestrator;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationRequestEvent event) {
        Notification saved = notificationService.save(
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );

        deliveryOrchestrator.deliver(saved);
    }
}
