package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.application.event.NotificationRequestEvent;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationOutboxDeliveryProcessorTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationDeviceService notificationDeviceService;

    @Mock
    private NotificationDeliveryOrchestrator notificationDeliveryOrchestrator;

    @Mock
    private NotificationOutboxService notificationOutboxService;

    @InjectMocks
    private NotificationOutboxDeliveryProcessor processor;

    @Test
    @DisplayName("성공 전송이면 outbox를 success 처리한다")
    void processFromEvent_WhenDeliverySuccess_MarksOutboxSuccess() {
        NotificationRequestEvent event = new NotificationRequestEvent(
                "outbox-1", "user-1", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-1"
        );
        Notification notification = mock(Notification.class);

        when(notificationService.save("user-1", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-1"))
                .thenReturn(notification);
        when(notification.getNotificationNo()).thenReturn("notification-1");
        when(notificationDeviceService.findByUserNo("user-1")).thenReturn(List.of());
        when(notificationDeliveryOrchestrator.deliver(notification, List.of()))
                .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(false, List.of()));

        processor.processFromEvent(event);

        verify(notificationOutboxService).success("outbox-1", "notification-1");
        verify(notificationOutboxService, never()).fail("outbox-1");
    }

    @Test
    @DisplayName("재시도 가능한 실패가 있으면 outbox를 fail 처리한다")
    void processFromEvent_WhenRetryableFailure_MarksOutboxFail() {
        NotificationRequestEvent event = new NotificationRequestEvent(
                "outbox-2", "user-2", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-2"
        );
        Notification notification = mock(Notification.class);

        when(notificationService.save("user-2", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-2"))
                .thenReturn(notification);
        when(notificationDeviceService.findByUserNo("user-2")).thenReturn(List.of());
        when(notificationDeliveryOrchestrator.deliver(notification, List.of()))
                .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(true, List.of()));

        processor.processFromEvent(event);

        verify(notificationOutboxService).fail("outbox-2");
        verify(notificationOutboxService, never()).success(anyString(), anyString());
    }

    @Test
    @DisplayName("영구 실패 토큰이 있으면 디바이스 토큰을 정리한다")
    void processFromOutbox_WhenInvalidTokens_ClearsInvalidTokens() {
        NotificationOutbox outbox = mock(NotificationOutbox.class);
        Notification notification = mock(Notification.class);

        when(outbox.getOutboxNo()).thenReturn("outbox-3");
        when(outbox.getRecipientNo()).thenReturn("user-3");
        when(outbox.getTitle()).thenReturn("title");
        when(outbox.getBody()).thenReturn("body");
        when(outbox.getType()).thenReturn(NotificationType.NEW_MESSAGE_RECEIVED);
        when(outbox.getRelatedId()).thenReturn("related-3");

        when(notificationService.save("user-3", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-3"))
                .thenReturn(notification);
        when(notification.getNotificationNo()).thenReturn("notification-3");
        when(notificationDeviceService.findByUserNo("user-3")).thenReturn(List.of());
        when(notificationDeliveryOrchestrator.deliver(notification, List.of()))
                .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(false, List.of("bad-token")));

        processor.processFromOutbox(outbox);

        verify(notificationDeviceService).clearInvalidFcmTokens(List.of("bad-token"));
        verify(notificationOutboxService).success("outbox-3", "notification-3");
    }

    @Test
    @DisplayName("예외가 발생하면 outbox를 fail 처리하고 예외를 외부로 전파하지 않는다")
    void processFromEvent_WhenExceptionThrown_FailsWithoutThrowing() {
        NotificationRequestEvent event = new NotificationRequestEvent(
                "outbox-4", "user-4", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-4"
        );
        RuntimeException exception = new RuntimeException("save failed");

        when(notificationService.save("user-4", "title", "body", NotificationType.NEW_MESSAGE_RECEIVED, "related-4"))
                .thenThrow(exception);

        processor.processFromEvent(event);

        verify(notificationOutboxService).fail("outbox-4");
    }
}
