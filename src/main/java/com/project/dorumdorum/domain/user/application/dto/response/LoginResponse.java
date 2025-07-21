package com.project.dorumdorum.domain.user.application.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}
