package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OnlineDeliveryStrategy implements NotificationDeliveryStrategy {

    @Override
    public NotificationDeliveryChannel getChannel(DecisionRequest request) {
        return NotificationDeliveryChannel.SSE;
    }
}
