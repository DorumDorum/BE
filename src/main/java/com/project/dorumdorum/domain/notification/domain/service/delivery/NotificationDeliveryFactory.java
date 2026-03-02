package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.infra.fcm.FcmNotificationDelivery;
import com.project.dorumdorum.domain.notification.infra.sse.SseNotificationDelivery;
import com.project.dorumdorum.global.exception.RestApiException;
import org.springframework.stereotype.Component;

import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.UNSUPPORTED_DELIVERY_CHANNEL;

@Component
public class NotificationDeliveryFactory {

    private final SseNotificationDelivery sseNotificationDelivery;
    private final FcmNotificationDelivery fcmNotificationDelivery;

    public NotificationDeliveryFactory(
            SseNotificationDelivery sseNotificationDelivery,
            FcmNotificationDelivery fcmNotificationDelivery
    ) {
        this.sseNotificationDelivery = sseNotificationDelivery;
        this.fcmNotificationDelivery = fcmNotificationDelivery;
    }

    public NotificationDelivery getDelivery(NotificationDeliveryChannel channel) {
        return switch (channel) {
            case SSE -> sseNotificationDelivery;
            case FCM -> fcmNotificationDelivery;
            default -> throw new RestApiException(UNSUPPORTED_DELIVERY_CHANNEL);
        };
    }
}
