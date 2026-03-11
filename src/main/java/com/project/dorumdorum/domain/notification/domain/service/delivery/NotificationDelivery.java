package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;

public interface NotificationDelivery {

    void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload, Device device);
}
