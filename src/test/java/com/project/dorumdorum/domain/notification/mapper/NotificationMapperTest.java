package com.project.dorumdorum.domain.notification.mapper;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationMapper 단위 테스트")
class NotificationMapperTest {

    // MapStruct에서 생성한 구현체를 직접 사용
    private final NotificationMapper mapper = new NotificationMapperImpl();

    @Test
    @DisplayName("toResponse는 read 필드를 isRead로 매핑하고 redirectPath를 계산한다")
    void toResponse_MapsFieldsAndRedirectPath() {
        // given
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();
        // 읽음 처리
        notification.markAsRead();

        // when
        LoadNotificationResponse response = mapper.toResponse(notification);

        // then
        assertThat(response.notificationNo()).isEqualTo("n1");
        assertThat(response.isRead()).isTrue();
        assertThat(response.redirectPath()).isEqualTo("/chats/room-1");
    }

    @Test
    @DisplayName("toDeliveryPayload는 redirectPath를 포함한 페이로드를 생성한다")
    void toDeliveryPayload_MapsFields() {
        // given
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.ROOM_APPLICATION_APPROVED)
                .relatedId("ignored") // 템플릿에 placeholder가 없으므로 무시
                .build();

        // when
        NotificationDeliveryPayload payload = mapper.toDeliveryPayload(notification);

        // then
        assertThat(payload.notificationNo()).isEqualTo("n1");
        assertThat(payload.title()).isEqualTo("title");
        assertThat(payload.type()).isEqualTo(NotificationType.ROOM_APPLICATION_APPROVED);
        assertThat(payload.redirectPath()).isEqualTo("/rooms/me");
    }

    @Test
    @DisplayName("resolveRedirectPath는 관련 ID가 없으면 null을 반환한다")
    void resolveRedirectPath_WithoutRelatedId_ReturnsNull() {
        NotificationMapper localMapper = new NotificationMapperImpl();

        String path = localMapper.resolveRedirectPath(
                NotificationType.NEW_MESSAGE_RECEIVED,
                null
        );

        assertThat(path).isNull();
    }
}

