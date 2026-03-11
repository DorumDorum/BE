package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryDecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDeliveryOrchestrator {

    private final NotificationDeliveryDecisionService decisionService;
    private final NotificationDeliveryFactory deliveryFactory;
    private final NotificationMapper notificationMapper;

    public void deliver(Notification notification, Device device) {
        NotificationDeliveryChannel channel = decisionService.decide(
                notification.getRecipientNo(),
                device.getDeviceId(),
                notification.getType(),
                notification.getRelatedId()
        );

        if (channel == NotificationDeliveryChannel.SKIP)
            return;

        NotificationDeliveryPayload payload = notificationMapper.toDeliveryPayload(notification);
        NotificationDelivery delivery = deliveryFactory.getDelivery(channel);
        delivery.send(channel, payload, device);
    }
}
