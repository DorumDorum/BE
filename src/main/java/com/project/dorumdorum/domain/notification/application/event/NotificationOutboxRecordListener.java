package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationOutboxRecordListener {

    private final NotificationOutboxService notificationOutboxService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(NotificationRequestEvent event) {
        notificationOutboxService.saveInit(
                event.outboxNo(),
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );
    }
}
