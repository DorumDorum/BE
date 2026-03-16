package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseNotificationDelivery {

    private final SseEmitterRegistry sseEmitterRegistry;

    public void send(NotificationDeliveryPayload payload, Device device) {
        sseEmitterRegistry.sendToDevice(payload.recipientNo(), device.getDeviceId(), payload);
    }
}
