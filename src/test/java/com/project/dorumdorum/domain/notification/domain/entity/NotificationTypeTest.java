package com.project.dorumdorum.domain.notification.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationType 열거형 단위 테스트")
class NotificationTypeTest {

    @Test
    @DisplayName("isChatNotification은 채팅 관련 알림에만 true를 반환한다")
    void isChatNotification_WorksAsExpected() {
        assertThat(NotificationType.CHAT_MESSAGE_REQUEST.isChatNotification()).isTrue();
        assertThat(NotificationType.CHAT_REQUEST_APPROVED.isChatNotification()).isTrue();
        assertThat(NotificationType.CHAT_REQUEST_REJECTED.isChatNotification()).isTrue();
        assertThat(NotificationType.NEW_MESSAGE_RECEIVED.isChatNotification()).isTrue();

        assertThat(NotificationType.ROOM_APPLICATION_APPROVED.isChatNotification()).isFalse();
        assertThat(NotificationType.ROOM_APPLICATION_REJECTED.isChatNotification()).isFalse();
        assertThat(NotificationType.ROOM_APPLICATION_RECEIVED.isChatNotification()).isFalse();
    }
}

