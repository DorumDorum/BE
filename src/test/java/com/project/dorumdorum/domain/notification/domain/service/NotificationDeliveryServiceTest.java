package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryOrchestrator;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.context.RetryContextSupport;
import org.springframework.retry.support.RetrySynchronizationManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.NOTIFICATION_FAILED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationDeliveryRetryService 단위 테스트")
class NotificationDeliveryServiceTest {

    @Mock
    private NotificationDeviceService notificationDeviceService;

    @Mock
    private NotificationDeliveryOrchestrator notificationDeliveryOrchestrator;

    @Mock
    private SystemAlertPublisher systemAlertPublisher;

    @InjectMocks
    private NotificationDeliveryService notificationDeliveryService;

    @Test
    @DisplayName("영구 실패 토큰은 정리한다")
    void deliver_WhenInvalidTokensExist_ClearsInvalidTokens() {
        Notification notification = Notification.builder()
                .notificationNo("notification-1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();

        when(notificationDeviceService.findByUserNo("user-1")).thenReturn(List.of());
        when(notificationDeliveryOrchestrator.deliver(notification, List.of()))
                .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(List.of(), List.of("bad-token")));

        notificationDeliveryService.deliver(notification);

        verify(notificationDeviceService).clearInvalidFcmTokens(List.of("bad-token"));
    }

    @Test
    @DisplayName("재시도 가능한 실패가 있으면 RestApiException을 던진다")
    void deliver_WhenRetryableFailureExists_ThrowsRestApiException() {
        Notification notification = Notification.builder()
                .notificationNo("notification-1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();

        when(notificationDeviceService.findByUserNo("user-1")).thenReturn(List.of());
        when(notificationDeliveryOrchestrator.deliver(notification, List.of()))
                .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(List.of("token-1"), List.of()));

        assertThatThrownBy(() -> notificationDeliveryService.deliver(notification))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("재시도 중이면 이전 실패 토큰에 해당하는 디바이스만 다시 조회한다")
    void deliver_WhenRetrying_UsesRetryableTokenDevicesOnly() {
        Notification notification = Notification.builder()
                .notificationNo("notification-1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();
        Device retryDevice = Device.builder()
                .userNo("user-1")
                .deviceId("device-2")
                .fcmToken("token-2")
                .build();
        RetryContextSupport retryContext = new RetryContextSupport(null);
        retryContext.setAttribute("retryableFcmTokens", List.of("token-2"));
        RetrySynchronizationManager.register(retryContext);

        try {
            when(notificationDeviceService.findByFcmTokens(List.of("token-2"))).thenReturn(List.of(retryDevice));
            when(notificationDeliveryOrchestrator.deliver(notification, List.of(retryDevice)))
                    .thenReturn(new NotificationDeliveryOrchestrator.DeliveryResult(List.of(), List.of()));

            notificationDeliveryService.deliver(notification);

            verify(notificationDeviceService).findByFcmTokens(List.of("token-2"));
        } finally {
            RetrySynchronizationManager.clear();
        }
    }

    @Test
    @DisplayName("최종 복구 시 디스코드 시스템 알림을 발행한다")
    void recover_PublishesSystemAlert() {
        Notification notification = Notification.builder()
                .notificationNo("notification-1")
                .recipientNo("user-1")
                .title("title")
                .body("body")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();

        notificationDeliveryService.recover(new RestApiException(NOTIFICATION_FAILED), notification);

        verify(systemAlertPublisher).publish(any(), any(), any(), any());
    }
}
