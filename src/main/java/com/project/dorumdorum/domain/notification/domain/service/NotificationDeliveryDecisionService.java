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

    private final UserPresenceService userPresenceService;
    private final NotificationDeliveryStrategyFactory strategyFactory;

    public NotificationDeliveryChannel decide(String recipientNo, NotificationType type, String relatedId) {
        UserPresence presence = userPresenceService.getPresence(recipientNo);
        NotificationDeliveryStrategy strategy = strategyFactory.getStrategy(presence);

        DecisionRequest request = new DecisionRequest(type, relatedId, presence);
        return strategy.getChannel(request);
    }
}
