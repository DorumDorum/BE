package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FcmNotificationDelivery 단위 테스트")
class FcmNotificationDeliveryTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private FcmNotificationDelivery delivery;

    private NotificationDeliveryPayload payload(String recipientNo) {
        return new NotificationDeliveryPayload(
                "n1",
                recipientNo,
                "title",
                "body",
                NotificationType.NEW_MESSAGE_RECEIVED,
                "room-1",
                "/chat/room-1"
        );
    }

    @Test
    @DisplayName("채널이 FCM이 아니면 아무 것도 전송하지 않는다")
    void send_WhenNotFcm_DoesNothing() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1");
        Device device = Device.builder().id("id1").userNo("u1").deviceId("d1").fcmToken("t1").build();

        delivery.send(NotificationDeliveryChannel.SSE, payload, device);

        verify(firebaseMessaging, never()).send(any(Message.class));
    }

    @Test
    @DisplayName("채널이 FCM이고 유효한 디바이스 토큰이 있으면 메시지를 보낸다")
    void send_WithDeviceToken_SendsMessage() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1");
        Device device = Device.builder().id("id1").userNo("user-1").deviceId("device-1").fcmToken("device-token").build();

        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id");

        delivery.send(NotificationDeliveryChannel.FCM, payload, device);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(firebaseMessaging).send(captor.capture());
        assertThat(captor.getValue()).isNotNull();
    }

    @Test
    @DisplayName("디바이스 토큰이 없으면 메시지를 보내지 않는다")
    void send_WithoutToken_DoesNotSend() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1");
        Device device = Device.builder().id("id1").userNo("user-1").deviceId("device-1").fcmToken("").build();

        delivery.send(NotificationDeliveryChannel.FCM, payload, device);

        verify(firebaseMessaging, never()).send(any(Message.class));
    }
}

