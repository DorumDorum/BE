package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.springframework.stereotype.Component;

@Component
public class NotificationDeliveryStrategyFactory {

    private final OfflineDeliveryStrategy offlineStrategy;
    private final OnlineDeliveryStrategy onlineStrategy;
    private final InChatroomDeliveryStrategy inChatroomStrategy;

    public NotificationDeliveryStrategyFactory(
            OfflineDeliveryStrategy offlineStrategy,
            OnlineDeliveryStrategy onlineStrategy,
            InChatroomDeliveryStrategy inChatroomStrategy
    ) {
        this.offlineStrategy = offlineStrategy;
        this.onlineStrategy = onlineStrategy;
        this.inChatroomStrategy = inChatroomStrategy;
    }

    public NotificationDeliveryStrategy getStrategy(UserPresence presence) {
        return switch (presence.kind()) {
            case OFFLINE -> offlineStrategy;
            case ONLINE -> onlineStrategy;
            case IN_CHATROOM -> inChatroomStrategy;
        };
    }
}
