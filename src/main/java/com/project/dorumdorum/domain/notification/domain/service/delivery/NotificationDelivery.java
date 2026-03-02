package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.vo.Device;

public interface NotificationDelivery {

    void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload);

    void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload, Device device);
}
