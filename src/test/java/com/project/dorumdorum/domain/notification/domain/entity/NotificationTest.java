package com.project.dorumdorum.domain.notification.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Notification 엔티티 단위 테스트")
class NotificationTest {

    @Test
    @DisplayName("처음 생성된 알림은 읽지 않음 상태이다")
    void isRead_DefaultFalse() {
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.ROOM_APPLICATION_APPROVED)
                .build();

        assertThat(notification.isRead()).isFalse();
    }

    @Test
    @DisplayName("markAsRead 호출 시 읽음 상태가 된다")
    void markAsRead_SetsReadAt() {
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.ROOM_APPLICATION_APPROVED)
                .build();

        notification.markAsRead();

        assertThat(notification.isRead()).isTrue();
    }
}

