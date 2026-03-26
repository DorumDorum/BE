package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.application.event.NotificationRequestEvent;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDispatchService {

    private final NotificationService notificationService;
    private final NotificationDeliveryService notificationDeliveryService;

    public void dispatch(NotificationRequestEvent event) {
        Notification notification = notificationService.save(
                event.recipientNo(),
                event.title(),
                event.body(),
                event.type(),
                event.relatedId()
        );

        notificationDeliveryService.deliver(notification);
    }
}
