package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final NotificationDeviceRepository notificationDeviceRepository;
    private final NotificationDeliveryOrchestrator deliveryOrchestrator;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationRequestEvent event) {
        Notification saved = notificationService.save(
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );

        for (var device : notificationDeviceRepository.findByUserNo(saved.getRecipientNo())) {
            deliveryOrchestrator.deliver(saved, device);
        }
    }
}
