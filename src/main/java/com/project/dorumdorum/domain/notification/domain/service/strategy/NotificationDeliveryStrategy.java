package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;

public interface NotificationDeliveryStrategy {

    NotificationDeliveryChannel getChannel(DecisionRequest request);
}
