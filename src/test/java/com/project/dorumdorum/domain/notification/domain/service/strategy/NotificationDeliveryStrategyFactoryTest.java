package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("NotificationDeliveryStrategyFactory 단위 테스트")
class NotificationDeliveryStrategyFactoryTest {

    private final OfflineDeliveryStrategy offline = mock(OfflineDeliveryStrategy.class);
    private final OnlineDeliveryStrategy online = mock(OnlineDeliveryStrategy.class);
    private final InChatroomDeliveryStrategy inChat = mock(InChatroomDeliveryStrategy.class);

    private final NotificationDeliveryStrategyFactory factory =
            new NotificationDeliveryStrategyFactory(offline, online, inChat);

    @Test
    @DisplayName("OFFLINE 상태면 OfflineDeliveryStrategy를 반환한다")
    void getStrategy_Offline() {
        NotificationDeliveryStrategy strategy = factory.getStrategy(UserPresence.offline());
        assertThat(strategy).isSameAs(offline);
    }

    @Test
    @DisplayName("ONLINE 상태면 OnlineDeliveryStrategy를 반환한다")
    void getStrategy_Online() {
        NotificationDeliveryStrategy strategy = factory.getStrategy(UserPresence.online());
        assertThat(strategy).isSameAs(online);
    }

    @Test
    @DisplayName("IN_CHATROOM 상태면 InChatroomDeliveryStrategy를 반환한다")
    void getStrategy_InChatroom() {
        NotificationDeliveryStrategy strategy = factory.getStrategy(UserPresence.inChatroom("room-1"));
        assertThat(strategy).isSameAs(inChat);
    }
}

