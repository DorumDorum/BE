package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryOutboxService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationOutboxRecordListener {

    private final NotificationOutboxService notificationOutboxService;
    private final NotificationDeviceRepository notificationDeviceRepository;
    private final NotificationDeliveryOutboxService notificationDeliveryOutboxService;

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

        List<Device> devices = notificationDeviceRepository.findByUserNo(event.recipientNo());
        notificationDeliveryOutboxService.saveInitByDevices(event.outboxNo(), devices);
    }
}
