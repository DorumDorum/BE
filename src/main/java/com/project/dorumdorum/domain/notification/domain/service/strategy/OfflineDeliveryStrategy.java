package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import org.springframework.stereotype.Component;

@Component
public class OfflineDeliveryStrategy implements NotificationDeliveryStrategy {

    @Override
    public NotificationDeliveryChannel getChannel(DecisionRequest request) {
        return NotificationDeliveryChannel.FCM;
    }
}
