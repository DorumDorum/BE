package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import com.project.dorumdorum.domain.notification.domain.entity.Device;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SseNotificationDelivery 단위 테스트")
class SseNotificationDeliveryTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @InjectMocks
    private SseNotificationDelivery delivery;

    private final NotificationDeliveryPayload payload =
            new NotificationDeliveryPayload(
                    "n1",
                    "user-1",
                    "title",
                    "body",
                    com.project.dorumdorum.domain.notification.domain.entity.NotificationType.ROOM_APPLICATION_APPROVED,
                    null,
                    null
            );

    @Test
    @DisplayName("디바이스 SSE 전송을 레지스트리에 위임한다")
    void send_DelegatesToRegistry() {
        Device device = Device.builder().id("id1").userNo("u1").deviceId("d1").fcmToken("t1").build();

        delivery.send(payload, device);

        verify(sseEmitterRegistry).sendToDevice("user-1", "d1", payload);
    }
}

