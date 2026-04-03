package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SseHeartbeatScheduler 단위 테스트")
class SseHeartbeatSchedulerTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @Mock
    private UserPresenceRepository userPresenceRepository;

    @InjectMocks
    private SseHeartbeatScheduler scheduler;

    @Test
    @DisplayName("run은 연결된 유저들에 대해 setOnline과 하트비트를 전송한다")
    void run_SendsHeartbeatAndKeepsOnline() {
        // given
        when(sseEmitterRegistry.getConnectedUsers()).thenReturn(Set.of("user-1", "user-2"));

        // when
        scheduler.run();

        // then
        verify(userPresenceRepository).refreshPresence("user-1");
        verify(userPresenceRepository).refreshPresence("user-2");
        verify(sseEmitterRegistry).sendHeartbeatToUser("user-1");
        verify(sseEmitterRegistry).sendHeartbeatToUser("user-2");
    }
}
