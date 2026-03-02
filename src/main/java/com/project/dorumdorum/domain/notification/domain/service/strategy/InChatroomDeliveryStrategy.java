package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InChatroomDeliveryStrategy implements NotificationDeliveryStrategy {

    @Override
    public NotificationDeliveryChannel getChannel(DecisionRequest request) {
        String currentMessageRoomNo = request.presence().messageRoomNo();

        if (request.type().isChatNotification()
                && Objects.equals(currentMessageRoomNo, request.relatedId()))
            return NotificationDeliveryChannel.SKIP;

        return request.hasSseConnection() ? NotificationDeliveryChannel.SSE : NotificationDeliveryChannel.FCM;
    }
}
