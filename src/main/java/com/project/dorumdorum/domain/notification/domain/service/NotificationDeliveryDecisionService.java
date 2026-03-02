package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import com.project.dorumdorum.domain.notification.domain.service.strategy.DecisionRequest;
import com.project.dorumdorum.domain.notification.domain.service.strategy.NotificationDeliveryStrategy;
import com.project.dorumdorum.domain.notification.domain.service.strategy.NotificationDeliveryStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDeliveryDecisionService {

    private final UserPresenceRepository userPresenceRepository;
    private final NotificationDeliveryStrategyFactory strategyFactory;
    private final SseConnectionChecker sseConnectionChecker;

    public NotificationDeliveryChannel decide(String recipientNo, String deviceId, NotificationType type, String relatedId) {
        UserPresence presence = userPresenceRepository.getPresence(recipientNo);
        NotificationDeliveryStrategy strategy = strategyFactory.getStrategy(presence);

        boolean hasSseConnection = sseConnectionChecker.hasConnection(recipientNo, deviceId);
        DecisionRequest request = new DecisionRequest(type, relatedId, presence, hasSseConnection);
        return strategy.getChannel(request);
    }
}
