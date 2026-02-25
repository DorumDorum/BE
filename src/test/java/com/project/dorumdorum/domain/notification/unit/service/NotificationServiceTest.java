package com.project.dorumdorum.domain.notification.unit.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Unit Tests")
class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FirebaseMessaging firebaseMessaging;
    @Mock
    private DomainEventLogger domainEventLogger;
    @Mock
    private SystemAlertPublisher systemAlertPublisher;

    @InjectMocks
    private NotificationService notificationService;

    private User user(String userNo, String token) {
        return User.builder()
                .userNo(userNo)
                .email("u@test.com")
                .password("pw")
                .role(Role.USER)
                .studentNo("20240001")
                .firebaseToken(token)
                .build();
    }

    @Test
    @DisplayName("Should save firebase token to user")
    void saveToken_UpdatesUserToken() {
        User user = user("u1", null);
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        notificationService.saveToken("u1", "new-token");

        assertThat(user.getFirebaseToken()).isEqualTo("new-token");
    }

    @Test
    @DisplayName("Should throw when saving token for missing user")
    void saveToken_WhenUserMissing_Throws() {
        when(userRepository.findById("u1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.saveToken("u1", "t"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should send notification successfully")
    void sendNotification_WhenSuccess_ReturnsMessageId() throws Exception {
        User user = user("u1", "token-1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(firebaseMessaging.send(any())).thenReturn("message-id");

        String result = notificationService.sendNotification(
                "u1", "title", "body", Map.of("k", "v"), "http://img"
        );

        assertThat(result).isEqualTo("message-id");
    }

    @Test
    @DisplayName("Should throw when receiver token is missing")
    void sendNotification_WhenTokenMissing_Throws() {
        User user = user("u1", " ");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                notificationService.sendNotification("u1", "title", "body", null, null))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Should clear token and throw when firebase token is invalid")
    void sendNotification_WhenTokenInvalid_ClearsTokenAndThrows() throws Exception {
        User user = user("u1", "old-token");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        FirebaseMessagingException ex = mock(FirebaseMessagingException.class);
        when(ex.getMessagingErrorCode()).thenReturn(MessagingErrorCode.UNREGISTERED);
        when(firebaseMessaging.send(any())).thenThrow(ex);

        assertThatThrownBy(() ->
                notificationService.sendNotification("u1", "title", "body", Map.of(), null))
                .isInstanceOf(RestApiException.class);
        assertThat(user.getFirebaseToken()).isNull();
    }

    @Test
    @DisplayName("Should throw notification failed on firebase error")
    void sendNotification_WhenOtherFirebaseError_ThrowsFailed() throws Exception {
        User user = user("u1", "token");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        FirebaseMessagingException ex = mock(FirebaseMessagingException.class);
        when(ex.getMessagingErrorCode()).thenReturn(MessagingErrorCode.INTERNAL);
        when(firebaseMessaging.send(any())).thenThrow(ex);

        assertThatThrownBy(() ->
                notificationService.sendNotification("u1", "title", "body", null, null))
                .isInstanceOf(RestApiException.class);
        assertThat(user.getFirebaseToken()).isEqualTo("token");
    }
}
