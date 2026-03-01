package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDelivery;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseNotificationDelivery implements NotificationDelivery {

    private final SseEmitterRegistry sseEmitterRegistry;

    @Override
    public void send(NotificationDeliveryChannel channel, NotificationDeliveryPayload payload) {
        if (channel != NotificationDeliveryChannel.SSE)
            return;

        sseEmitterRegistry.sendToUser(payload.recipientNo(), payload);
    }
}
