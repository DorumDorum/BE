package com.project.dorumdorum.domain.notification.infra.sse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SseConnectionCheckerImpl 단위 테스트")
class SseConnectionCheckerImplTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @InjectMocks
    private SseConnectionCheckerImpl checker;

    @Test
    @DisplayName("hasConnection은 SseEmitterRegistry에 위임한다")
    void hasConnection_DelegatesToRegistry() {
        when(sseEmitterRegistry.hasConnection("user-1", "device-1")).thenReturn(true);

        boolean result = checker.hasConnection("user-1", "device-1");

        assertThat(result).isTrue();
        verify(sseEmitterRegistry).hasConnection("user-1", "device-1");
    }
}

