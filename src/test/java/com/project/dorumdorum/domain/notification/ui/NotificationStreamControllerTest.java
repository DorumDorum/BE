package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.RegisterDeviceTokenUseCase;
import com.project.dorumdorum.domain.notification.infra.sse.SseEmitterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationStreamController 단위 테스트")
class NotificationStreamControllerTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @Mock
    private RegisterDeviceTokenUseCase registerDeviceTokenUseCase;

    @InjectMocks
    private NotificationStreamController controller;

    @Test
    @DisplayName("userNo와 deviceId가 주어지면 디바이스 등록 후 SseEmitterRegistry에 등록한다")
    void stream_WithUserNoAndDeviceId_RegistersSse() {
        // given
        String userNo = "user-1";
        String deviceId = "device-1";
        SseEmitter emitter = new SseEmitter();
        when(sseEmitterRegistry.register(userNo, deviceId)).thenReturn(emitter);

        // when
        SseEmitter result = controller.stream(userNo, deviceId);

        // then
        assertThat(result).isEqualTo(emitter);
        verify(registerDeviceTokenUseCase).execute(userNo, deviceId, "");
        verify(sseEmitterRegistry).register(userNo, deviceId);
    }
}
