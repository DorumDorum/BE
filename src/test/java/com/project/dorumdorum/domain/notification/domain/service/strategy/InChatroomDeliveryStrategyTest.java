package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InChatroomDeliveryStrategy 단위 테스트")
class InChatroomDeliveryStrategyTest {

    private final InChatroomDeliveryStrategy strategy = new InChatroomDeliveryStrategy();

    @Test
    @DisplayName("채팅 알림이고 현재 입장한 방과 동일하면 SKIP을 반환한다")
    void getChannel_WhenChatNotificationInSameRoom_ReturnsSkip() {
        // given
        UserPresence presence = UserPresence.inChatroom("room-1");
        DecisionRequest request = new DecisionRequest(
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-1",
                presence,
                true
        );

        // when
        NotificationDeliveryChannel channel = strategy.getChannel(request);

        // then
        assertThat(channel).isEqualTo(NotificationDeliveryChannel.SKIP);
    }

    @Test
    @DisplayName("채팅 알림이지만 다른 방이면 SSE 또는 FCM으로 전송한다 - SSE 연결 있음")
    void getChannel_WhenChatNotificationDifferentRoomAndHasSse_ReturnsSse() {
        // given
        UserPresence presence = UserPresence.inChatroom("room-1");
        DecisionRequest request = new DecisionRequest(
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-2",
                presence,
                true
        );

        // when
        NotificationDeliveryChannel channel = strategy.getChannel(request);

        // then
        assertThat(channel).isEqualTo(NotificationDeliveryChannel.SSE);
    }

    @Test
    @DisplayName("채팅 알림이지만 다른 방이면 SSE 또는 FCM으로 전송한다 - SSE 연결 없음")
    void getChannel_WhenChatNotificationDifferentRoomAndNoSse_ReturnsFcm() {
        // given
        UserPresence presence = UserPresence.inChatroom("room-1");
        DecisionRequest request = new DecisionRequest(
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-2",
                presence,
                false
        );

        // when
        NotificationDeliveryChannel channel = strategy.getChannel(request);

        // then
        assertThat(channel).isEqualTo(NotificationDeliveryChannel.FCM);
    }
}

