package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OnlineDeliveryStrategy 단위 테스트")
class OnlineDeliveryStrategyTest {

    private final OnlineDeliveryStrategy strategy = new OnlineDeliveryStrategy();

    @Test
    @DisplayName("SSE 연결이 있으면 SSE 채널을 선택한다")
    void getChannel_WithSseConnection_ReturnsSse() {
        // given
        DecisionRequest request = new DecisionRequest(
                NotificationType.ROOM_APPLICATION_APPROVED,
                null,
                UserPresence.online(),
                true
        );

        // when
        NotificationDeliveryChannel channel = strategy.getChannel(request);

        // then
        assertThat(channel).isEqualTo(NotificationDeliveryChannel.SSE);
    }

    @Test
    @DisplayName("SSE 연결이 없으면 FCM 채널을 선택한다")
    void getChannel_WithoutSseConnection_ReturnsFcm() {
        // given
        DecisionRequest request = new DecisionRequest(
                NotificationType.ROOM_APPLICATION_APPROVED,
                null,
                UserPresence.online(),
                false
        );

        // when
        NotificationDeliveryChannel channel = strategy.getChannel(request);

        // then
        assertThat(channel).isEqualTo(NotificationDeliveryChannel.FCM);
    }
}

