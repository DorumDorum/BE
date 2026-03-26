package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.service.NotificationDispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationRequestListener {

    private final NotificationDispatchService notificationDispatchService;

    @Async("notificationExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void handle(NotificationRequestEvent event) {
        notificationDispatchService.dispatch(event);
    }
}
