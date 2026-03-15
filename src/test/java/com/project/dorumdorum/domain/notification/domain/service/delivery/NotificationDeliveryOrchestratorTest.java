package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryDecisionService;
import com.project.dorumdorum.domain.notification.infra.fcm.FcmNotificationDelivery;
import com.project.dorumdorum.domain.notification.infra.sse.SseNotificationDelivery;
import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationDeliveryOrchestrator 단위 테스트")
class NotificationDeliveryOrchestratorTest {

    @Mock
    private NotificationDeliveryDecisionService decisionService;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private SseNotificationDelivery sseNotificationDelivery;

    @Mock
    private FcmNotificationDelivery fcmNotificationDelivery;

    @InjectMocks
    private NotificationDeliveryOrchestrator orchestrator;

    @Test
    @DisplayName("디바이스별 채널에 따라 SSE는 즉시 전송하고 FCM은 멀티캐스트로 묶는다")
    void deliver_ClassifiesDevicesAndDelegatesPerChannel() {
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();
        Device sseDevice = Device.builder().id("id1").userNo("user-1").deviceId("device-sse").fcmToken("token-sse").build();
        Device fcmDevice = Device.builder().id("id2").userNo("user-1").deviceId("device-fcm").fcmToken("token-fcm").build();
        Device skipDevice = Device.builder().id("id3").userNo("user-1").deviceId("device-skip").fcmToken("token-skip").build();

        NotificationDeliveryPayload payload = new NotificationDeliveryPayload(
                "n1", "user-1", "title", "body",
                NotificationType.NEW_MESSAGE_RECEIVED, "room-1", "/chat/room-1"
        );
        when(notificationMapper.toDeliveryPayload(notification)).thenReturn(payload);
        when(decisionService.decide("user-1", "device-sse", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(NotificationDeliveryChannel.SSE);
        when(decisionService.decide("user-1", "device-fcm", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(NotificationDeliveryChannel.FCM);
        when(decisionService.decide("user-1", "device-skip", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(NotificationDeliveryChannel.SKIP);
        when(fcmNotificationDelivery.sendMulticast(payload, List.of("token-fcm")))
                .thenReturn(new FcmNotificationDelivery.MulticastSendResult(0, List.of()));

        NotificationDeliveryOrchestrator.DeliveryResult result = orchestrator.deliver(
                notification, List.of(sseDevice, fcmDevice, skipDevice)
        );

        verify(notificationMapper).toDeliveryPayload(notification);
        verify(sseNotificationDelivery).send(payload, sseDevice);
        verify(fcmNotificationDelivery).sendMulticast(payload, List.of("token-fcm"));
        org.junit.jupiter.api.Assertions.assertFalse(result.hasRetryableFailure());
        org.junit.jupiter.api.Assertions.assertTrue(result.invalidTokens().isEmpty());
    }

    @Test
    @DisplayName("FCM 멀티캐스트 결과의 실패 정보를 DeliveryResult로 반환한다")
    void deliver_MapsMulticastResultToDeliveryResult() {
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();
        Device device = Device.builder().id("id1").userNo("user-1").deviceId("device-1").fcmToken("token-1").build();

        NotificationDeliveryPayload payload = new NotificationDeliveryPayload(
                "n1", "user-1", "title", "body",
                NotificationType.NEW_MESSAGE_RECEIVED, "room-1", "/chat/room-1"
        );
        when(decisionService.decide("user-1", "device-1", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(NotificationDeliveryChannel.FCM);
        when(notificationMapper.toDeliveryPayload(notification)).thenReturn(payload);
        when(fcmNotificationDelivery.sendMulticast(payload, List.of("token-1")))
                .thenReturn(new FcmNotificationDelivery.MulticastSendResult(1, List.of("token-1")));

        NotificationDeliveryOrchestrator.DeliveryResult result = orchestrator.deliver(notification, List.of(device));

        verify(fcmNotificationDelivery).sendMulticast(eq(payload), eq(List.of("token-1")));
        org.junit.jupiter.api.Assertions.assertTrue(result.hasRetryableFailure());
        org.junit.jupiter.api.Assertions.assertEquals(List.of("token-1"), result.invalidTokens());
    }
}

