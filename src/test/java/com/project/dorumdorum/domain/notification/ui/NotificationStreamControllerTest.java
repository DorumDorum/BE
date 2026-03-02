package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.infra.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationStreamController 단위 테스트")
class NotificationStreamControllerTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private NotificationStreamController controller;

    @Test
    @DisplayName("유효한 토큰과 deviceId가 있으면 SSE 등록을 수행한다")
    void stream_WithValidTokenAndDeviceId_RegistersSse() {
        // given
        when(request.getParameter("accessToken")).thenReturn("token");
        when(request.getParameter("deviceId")).thenReturn("device-1");
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.getId("token")).thenReturn(Optional.of("user-1"));
        SseEmitter emitter = new SseEmitter();
        when(sseEmitterRegistry.register("user-1", "device-1")).thenReturn(emitter);

        // when
        SseEmitter result = controller.stream(request);

        // then
        assertThat(result).isEqualTo(emitter);
        verify(sseEmitterRegistry).register("user-1", "device-1");
    }

    @Test
    @DisplayName("accessToken이 없으면 예외를 던진다")
    void stream_WithoutToken_Throws() {
        when(request.getParameter("accessToken")).thenReturn(null);

        assertThatThrownBy(() -> controller.stream(request))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("deviceId가 없으면 예외를 던진다")
    void stream_WithoutDeviceId_Throws() {
        when(request.getParameter("accessToken")).thenReturn("token");
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.getId("token")).thenReturn(Optional.of("user-1"));
        when(request.getParameter("deviceId")).thenReturn("");

        assertThatThrownBy(() -> controller.stream(request))
                .isInstanceOf(RestApiException.class);
    }
}

