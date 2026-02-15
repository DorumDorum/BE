package com.project.dorumdorum.domain.notification.unit.dto;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SendFirebaseTokenRequest Unit Tests")
class SendFirebaseTokenRequestTest {

    @Test
    @DisplayName("Should expose firebase token value")
    void record_AccessorWorks() {
        SendFirebaseTokenRequest request = new SendFirebaseTokenRequest("firebase-token");

        assertThat(request.firebaseToken()).isEqualTo("firebase-token");
    }
}
