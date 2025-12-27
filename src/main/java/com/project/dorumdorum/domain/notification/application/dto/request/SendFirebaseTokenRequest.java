package com.project.dorumdorum.domain.notification.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendFirebaseTokenRequest(
        @NotBlank String firebaseToken
) {
}

