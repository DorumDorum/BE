package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDispatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationRequestListener 단위 테스트")
class NotificationRequestListenerTest {

    @Mock
    private NotificationDispatchService notificationDispatchService;

    @InjectMocks
    private NotificationRequestListener listener;

    @Test
    @DisplayName("handle은 이벤트 처리를 outbox 프로세서에 위임한다")
    void handle_DelegatesToProcessor() {
        NotificationRequestEvent event = new NotificationRequestEvent(
                "user-1",
                "title",
                "body",
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-1"
        );

        listener.handle(event);

        verify(notificationDispatchService).dispatch(event);
    }
}
