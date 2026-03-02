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
        return request.hasSseConnection()
                ? NotificationDeliveryChannel.SSE   // 유저의 해당 디바이스가 온라인일 경우
                : NotificationDeliveryChannel.FCM;  // 유저의 해당 디바이스가 오프라인일 경우
    }
}
