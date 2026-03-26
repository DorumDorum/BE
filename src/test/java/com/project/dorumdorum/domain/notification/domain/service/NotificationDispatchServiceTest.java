package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.application.event.NotificationRequestEvent;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationDispatchService 단위 테스트")
class NotificationDispatchServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationDeliveryService notificationDeliveryService;

    @InjectMocks
    private NotificationDispatchService notificationDispatchService;

    @Test
    @DisplayName("이벤트를 알림 저장 후 재시도 전송 서비스에 전달한다")
    void dispatch_SavesNotificationThenDelivers() {
        NotificationRequestEvent event = new NotificationRequestEvent(
                "user-1",
                "title",
                "body",
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-1"
        );
        Notification notification = Notification.builder()
                .notificationNo("notification-1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();

        when(notificationService.save("user-1", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(notification);

        notificationDispatchService.dispatch(event);

        verify(notificationService).save("user-1", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "room-1");
        verify(notificationDeliveryService).deliver(notification);
    }
}
