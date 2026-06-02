package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService 단위 테스트")
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("save는 전달받은 값으로 Notification을 생성하고 저장한다")
    void save_PersistsNotification() {
        // given
        NotificationType type = NotificationType.ROOM_APPLICATION_APPROVED;
        Notification saved = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(type)
                .relatedId("r1")
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        // when
        Notification result = notificationService.save("user-1", "title", "body", type, "r1");

        // then
        assertThat(result).isEqualTo(saved);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("searchByCursor는 레포지토리 메서드에 위임한다")
    void searchByCursor_DelegatesToRepository() {
        // given
        LocalDateTime cursorCreatedAt = LocalDateTime.now();
        List<Notification> notifications = List.of(Notification.builder().build());
        when(notificationRepository.findByCursor("user-1", cursorCreatedAt, "n1", 21))
                .thenReturn(notifications);

        // when
        List<Notification> result = notificationService.searchByCursor("user-1", cursorCreatedAt, "n1", 21);

        // then
        assertThat(result).isEqualTo(notifications);
        verify(notificationRepository).findByCursor("user-1", cursorCreatedAt, "n1", 21);
    }

    @Test
    @DisplayName("markAsRead는 알림을 조회해 읽음 처리하고 저장한다")
    void markAsRead_WhenFound_MarksAndSaves() {
        // given
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .build();
        when(notificationRepository.findByNotificationNoAndRecipientNo("n1", "user-1"))
                .thenReturn(Optional.of(notification));

        // when
        notificationService.markAsRead("n1", "user-1");

        // then
        verify(notificationRepository).findByNotificationNoAndRecipientNo("n1", "user-1");
        verify(notificationRepository).save(eq(notification));
    }

    @Test
    @DisplayName("markAsRead는 알림이 없으면 예외를 던진다")
    void markAsRead_WhenMissing_Throws() {
        // given
        when(notificationRepository.findByNotificationNoAndRecipientNo("n1", "user-1"))
                .thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> notificationService.markAsRead("n1", "user-1"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("markAllAsRead는 사용자의 읽지 않은 알림 벌크 업데이트를 레포지토리에 위임한다")
    void markAllAsRead_DelegatesToRepository() {
        // given
        when(notificationRepository.markAllAsReadByRecipientNo(eq("user-1"), any(LocalDateTime.class)))
                .thenReturn(2);

        // when
        int result = notificationService.markAllAsRead("user-1");

        // then
        assertThat(result).isEqualTo(2);
        verify(notificationRepository).markAllAsReadByRecipientNo(eq("user-1"), any(LocalDateTime.class));
    }
}
