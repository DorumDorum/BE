package com.project.dorumdorum.domain.notification.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterDeviceTokenRequest(
        @NotBlank(message = "deviceId는 필수입니다")
        String deviceId,
        String fcmToken
) {}
