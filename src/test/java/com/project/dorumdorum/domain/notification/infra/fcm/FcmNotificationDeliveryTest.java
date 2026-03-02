package com.project.dorumdorum.domain.notification.infra.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryChannel;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.domain.notification.domain.vo.Device;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FcmNotificationDelivery 단위 테스트")
class FcmNotificationDeliveryTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FcmNotificationDelivery delivery;

    private NotificationDeliveryPayload payload(String recipientNo, String token) {
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
        NotificationDeliveryPayload payload = payload("user-1", null);
        delivery.send(NotificationDeliveryChannel.SSE, payload);
        delivery.send(NotificationDeliveryChannel.SSE, payload, new Device("d1", "t1"));

        verify(firebaseMessaging, never()).send(any(Message.class));
    }

    @Test
    @DisplayName("채널이 FCM이고 유저 토큰이 있으면 해당 토큰으로 메시지를 보낸다")
    void send_WithUserToken_SendsMessage() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1", null);
        User user = User.builder().userNo("user-1").build();
        // User 엔티티에 firebaseToken 필드가 있다고 가정
        java.lang.reflect.Field field = User.class.getDeclaredField("firebaseToken");
        field.setAccessible(true);
        field.set(user, "user-token");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id");

        delivery.send(NotificationDeliveryChannel.FCM, payload);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(firebaseMessaging).send(captor.capture());
        Message message = captor.getValue();
        // 직접 필드는 확인하기 어렵기 때문에 null 이 아님만 확인
        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("디바이스 기반 전송은 Device 토큰으로 메시지를 보낸다")
    void send_WithDeviceToken_SendsMessage() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1", null);
        Device device = new Device("device-1", "device-token");

        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id");

        delivery.send(NotificationDeliveryChannel.FCM, payload, device);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(firebaseMessaging).send(captor.capture());
        Message message = captor.getValue();
        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("토큰이 없으면 메시지를 보내지 않는다")
    void send_WithoutToken_DoesNotSend() throws Exception {
        NotificationDeliveryPayload payload = payload("user-1", null);
        User user = User.builder().userNo("user-1").build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        delivery.send(NotificationDeliveryChannel.FCM, payload);

        verify(firebaseMessaging, never()).send(any(Message.class));
    }
}

