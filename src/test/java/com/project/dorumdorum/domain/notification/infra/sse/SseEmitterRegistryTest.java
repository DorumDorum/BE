package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SseEmitterRegistry 단위 테스트")
class SseEmitterRegistryTest {

    @Mock
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Mock
    private UserPresenceRepository userPresenceRepository;

    @InjectMocks
    private SseEmitterRegistry registry;

    @Test
    @DisplayName("register는 첫 연결 시 ONLINE으로 설정하고 hasConnection이 true를 반환한다")
    void register_FirstConnection_SetsOnlineAndHasConnection() {
        SseEmitter emitter = registry.register("user-1", "device-1");

        assertThat(emitter).isNotNull();
        verify(userPresenceRepository).setOnline("user-1");
        assertThat(registry.hasConnection("user-1", "device-1")).isTrue();
        assertThat(registry.getConnectedUsers()).contains("user-1");
    }

    @Test
    @DisplayName("sendToUser는 연결이 없는 경우 아무 것도 하지 않는다")
    void sendToUser_NoConnections_DoesNothing() {
        NotificationDeliveryPayload payload = new NotificationDeliveryPayload(
                "n1", "user-1", "t", "b", null, null, null
        );
        registry.sendToUser("user-1", payload);
        // 예외가 나지 않으면 성공으로 간주
    }
}

