package com.project.dorumdorum.domain.notification.unit.application;

import com.project.dorumdorum.domain.chat.presence.PresenceService;
import com.project.dorumdorum.domain.chat.presence.PresenceSnapshot;
import com.project.dorumdorum.domain.chat.presence.PresenceStatus;
import com.project.dorumdorum.domain.notification.application.NotificationSseService;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationSseService Unit Tests")
class NotificationSseServiceTest {

    @Mock
    private SseEmitterRegistry emitterRegistry;
    @Mock
    private PresenceService presenceService;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private NotificationSseService service;

    @Test
    @DisplayName("Should connect and set app active when status is not in-room")
    void connect_WhenAuthenticatedAndNotInRoom_SetsActive() throws IOException {
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.isAccessToken("token")).thenReturn(true);
        when(tokenProvider.getId("token")).thenReturn(Optional.of("u1"));
        when(presenceService.getPresence("u1")).thenReturn(PresenceSnapshot.appInactive("u1"));

        SseEmitter emitter = service.connect("token", 1000L);

        assertThat(emitter).isNotNull();
        verify(emitterRegistry).register(eq("u1"), any(SseEmitter.class));
        verify(presenceService).setAppActive("u1");
    }

    @Test
    @DisplayName("Should not set app active when already in room")
    void connect_WhenInRoom_DoesNotSetActive() throws IOException {
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.isAccessToken("token")).thenReturn(true);
        when(tokenProvider.getId("token")).thenReturn(Optional.of("u1"));
        when(presenceService.getPresence("u1")).thenReturn(PresenceSnapshot.inRoom("u1", "room1"));

        service.connect("token", 1000L);

        verify(presenceService, never()).setAppActive("u1");
    }

    @Test
    @DisplayName("Should throw unauthorized when token is blank")
    void connect_WhenBlankToken_Throws() {
        assertThatThrownBy(() -> service.connect(" ", 1000L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should throw unauthorized when token is invalid")
    void connect_WhenTokenInvalid_Throws() {
        when(tokenProvider.validateToken("bad")).thenReturn(false);

        assertThatThrownBy(() -> service.connect("bad", 1000L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should throw unauthorized when access token check fails")
    void connect_WhenNotAccessToken_Throws() {
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.isAccessToken("token")).thenReturn(false);

        assertThatThrownBy(() -> service.connect("token", 1000L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should throw unauthorized when token id missing")
    void connect_WhenIdMissing_Throws() {
        when(tokenProvider.validateToken("token")).thenReturn(true);
        when(tokenProvider.isAccessToken("token")).thenReturn(true);
        when(tokenProvider.getId("token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.connect("token", 1000L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should remove emitter and set inactive on disconnect")
    void onDisconnect_WhenAppActive_RemovesAndSetsInactive() {
        when(presenceService.getPresence("u1"))
                .thenReturn(new PresenceSnapshot("u1", PresenceStatus.APP_ACTIVE, null, java.time.LocalDateTime.now()));

        ReflectionTestUtils.invokeMethod(service, "onDisconnect", "u1");

        verify(emitterRegistry).remove("u1");
        verify(presenceService).setAppInactive("u1");
    }

    @Test
    @DisplayName("Should remove emitter only when status is not app active")
    void onDisconnect_WhenNotAppActive_RemovesOnly() {
        when(presenceService.getPresence("u1")).thenReturn(PresenceSnapshot.inRoom("u1", "room1"));

        ReflectionTestUtils.invokeMethod(service, "onDisconnect", "u1");

        verify(emitterRegistry).remove("u1");
        verify(presenceService, never()).setAppInactive("u1");
    }
}
