package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.NotificationDeliveryDecisionService;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.mapper.NotificationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationDeliveryOrchestrator 단위 테스트")
class NotificationDeliveryOrchestratorTest {

    @Mock
    private NotificationDeliveryDecisionService decisionService;

    @Mock
    private NotificationDeliveryFactory deliveryFactory;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationDelivery delivery;

    @InjectMocks
    private NotificationDeliveryOrchestrator orchestrator;

    @Test
    @DisplayName("채널이 SKIP이면 전송을 수행하지 않는다")
    void deliver_WhenChannelSkip_DoesNothing() {
        Notification notification = Notification.builder()
                .notificationNo("n1")
                .recipientNo("user-1")
                .type(NotificationType.NEW_MESSAGE_RECEIVED)
                .relatedId("room-1")
                .build();
        Device device = Device.builder().id("id1").userNo("user-1").deviceId("device-1").fcmToken("token-1").build();

        when(decisionService.decide("user-1", "device-1", NotificationType.NEW_MESSAGE_RECEIVED, "room-1"))
                .thenReturn(NotificationDeliveryChannel.SKIP);

        orchestrator.deliver(notification, device);

        verify(notificationMapper, never()).toDeliveryPayload(any());
        verify(deliveryFactory, never()).getDelivery(any());
        verify(delivery, never()).send(any(), any(), any(Device.class));
    }

    @Test
    @DisplayName("채널이 SSE/FCM이면 페이로드를 생성하고 적절한 Delivery에 위임한다")
    void deliver_WhenChannelIsNotSkip_DelegatesToDelivery() {
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
                .thenReturn(NotificationDeliveryChannel.SSE);
        when(notificationMapper.toDeliveryPayload(notification)).thenReturn(payload);
        when(deliveryFactory.getDelivery(NotificationDeliveryChannel.SSE)).thenReturn(delivery);

        orchestrator.deliver(notification, device);

        verify(notificationMapper).toDeliveryPayload(notification);
        verify(deliveryFactory).getDelivery(NotificationDeliveryChannel.SSE);
        verify(delivery).send(NotificationDeliveryChannel.SSE, payload, device);
    }
}

